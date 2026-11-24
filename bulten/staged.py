"""Adımlı bülten: API çağrısı yok. Seçim ve yazım işini dışarıdaki bir ajan (ör. Claude rutini) yapar.

Akış:
  python -m bulten staged prep [--variant haiku] [--until 2026-11-23T23:58Z] [--date 2026-11-24] [--compare]
      haberleri toplar, work/tasks/ altına görev dosyaları yazar
  (ajan her work/tasks/X.md dosyasını okuyup cevabını work/out/X.json olarak yazar)
  python -m bulten staged next
      cevapları okur, sonraki görevleri yazar; en sonda bülteni kaydeder ve "BİTTİ" yazar

--compare: karşılaştırma modu. Bülten data/compare/ altına yazılır, geçmiş (history.json) değişmez,
           o günün mevcut bülteni "önceden verilmiş" sayılmaz, Telegram'a hiçbir şey gitmez.
"""
from __future__ import annotations

import json
import shutil
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone, timedelta

from . import sources as S
from .common import ROOT, DATA, read_json, write_json, now_tr, load_config
from .daily import (SELECT_SYSTEM, DEDUPE_SYSTEM, DETAIL_SYSTEM, collect, pick_candidates,
                    select_prompt, parse_selection, keep_stories, dedupe_listing, apply_dedupe,
                    _grounding_texts, detail_prompt, build_detail, assemble)
from .markets import fetch_markets

WORK = ROOT / "work"
TASKS = WORK / "tasks"
OUT = WORK / "out"
STATE = WORK / "state.json"

TASK_HEADER = """# Görev: {name}

Aşağıdaki SİSTEM TALİMATI ve GİRDİ'yi oku, talimata göre cevap üret.
Cevabını SADECE geçerli JSON olarak şu dosyaya yaz: `{out}`
JSON dışında hiçbir şey yazma (açıklama, markdown kod bloğu yok). Dosyada başka bir şey olmasın.

## SİSTEM TALİMATI

{system}

## GİRDİ

{user}
"""


def _write_task(name: str, system: str, user: str) -> str:
    out = f"work/out/{name}.json"
    (TASKS / f"{name}.md").write_text(TASK_HEADER.format(name=name, out=out, system=system, user=user),
                                       encoding="utf-8")
    return name


def _print_tasks(names: list[str]) -> None:
    print(f"GÖREVLER ({len(names)}):")
    for n in names:
        print(f"  work/tasks/{n}.md -> work/out/{n}.json")
    print("Hepsini bitirince: python -m bulten staged next")


def _read_out(name: str) -> dict | None:
    p = OUT / f"{name}.json"
    if not p.exists():
        print(f"  cevap yok: {name}")
        return None
    txt = p.read_text(encoding="utf-8").strip()
    if txt.startswith("```"):
        txt = txt.strip("`").removeprefix("json").strip()
    try:
        return json.loads(txt)
    except json.JSONDecodeError as e:
        print(f"  geçersiz JSON: {name}: {e}")
        return None


def _save(state: dict) -> None:
    write_json(STATE, state)


def _load_pool(state: dict) -> S.Pool:
    pool = S.Pool()
    pool.items = [S.Item(**d) for d in state["pool"]]
    return pool


def _chars(names: list[str]) -> int:
    return sum((TASKS / f"{n}.md").stat().st_size for n in names if (TASKS / f"{n}.md").exists())


def prep(variant: str, until: str | None, date: str | None, compare: bool) -> None:
    cfg = load_config()
    if WORK.exists():
        shutil.rmtree(WORK)
    TASKS.mkdir(parents=True)
    OUT.mkdir(parents=True)

    started = datetime.now(timezone.utc)
    end = datetime.strptime(until, "%Y-%m-%dT%H:%MZ").replace(tzinfo=timezone.utc) if until else started
    since = end - timedelta(hours=cfg.get("lookback_hours", 30))
    date = date or now_tr().strftime("%Y-%m-%d")

    hist = read_json(DATA / "history.json", {"cited_urls": {}, "headlines": {}})
    if compare:  # o günün mevcut bülteni "önceden verilmiş" sayılmasın
        hist = {"cited_urls": {u: d for u, d in hist["cited_urls"].items() if d < date},
                "headlines": {k: [h for h in v if h["date"] < date] for k, v in hist["headlines"].items()}}

    print("Kaynaklar taranıyor...")
    pool = S.Pool()
    topic_ids = collect(cfg, since, pool)
    if until:
        cutoff = end.strftime("%Y-%m-%dT%H:%M")
        late = {it.id for it in pool.items if it.published and it.published[:16] > cutoff}
        topic_ids = {k: [[i for i in g if i not in late] for g in groups] for k, groups in topic_ids.items()}
        print(f"Kesim saatinden ({until}) sonraki {len(late)} haber çıkarıldı")
    print(f"Toplam tekil haber: {len(pool.items)}")

    try:
        markets = fetch_markets(cfg.get("markets", []))
    except Exception as e:
        print("Piyasa verisi alınamadı:", e)
        markets = []

    limit = cfg.get("max_candidates_per_topic", 220)
    cited = set(hist["cited_urls"])
    cands, names = {}, []
    for t in cfg["topics"]:
        ids = pick_candidates(pool, topic_ids[t["id"]], limit, cited)
        cands[t["id"]] = ids
        user = select_prompt(t, pool, ids, hist["headlines"].get(t["id"], []), markets)
        names.append(_write_task(f"secim-{t['id']}", SELECT_SYSTEM, user))

    _save({"stage": "select", "variant": variant, "date": date, "compare": compare,
           "since": since.isoformat(), "started": started.isoformat(),
           "pool": [it.to_dict() for it in pool.items], "cands": cands, "markets": markets,
           "task_chars": _chars(names), "tasks": len(names)})
    _print_tasks(names)


def next_step() -> None:
    state = read_json(STATE)
    if not state:
        raise SystemExit("Önce: python -m bulten staged prep")
    cfg = load_config()
    topics = cfg["topics"]
    stage = state["stage"]

    if stage == "select":
        sel = {}
        for t in topics:
            res = _read_out(f"secim-{t['id']}")
            ids = state["cands"].get(t["id"], [])
            sel[t["id"]] = (None if res is None else parse_selection(res, ids), len(ids))
        kept = keep_stories(topics, sel)
        state["sel"] = {k: [v[0], v[1]] for k, v in sel.items()}
        state["kept"] = kept
        flat = [(t, s) for t in topics for s in kept[t["id"]]]
        if len(flat) >= 2:
            state["stage"] = "dedupe"
            _save(state)
            names = [_write_task("tekillestirme", DEDUPE_SYSTEM, dedupe_listing(flat))]
            state["task_chars"] += _chars(names)
            state["tasks"] += 1
            _save(state)
            _print_tasks(names)
            return
        stage = "dedupe"
        state["skip_dedupe"] = True

    if stage == "dedupe":
        kept = state["kept"]
        if not state.get("skip_dedupe"):
            flat = [(t, s) for t in topics for s in kept[t["id"]]]
            res = _read_out("tekillestirme")
            if res is not None:
                kept = apply_dedupe(res, topics, flat)
        pool = _load_pool(state)
        since = datetime.fromisoformat(state["since"])
        jobs = [(t, s) for t in topics for s in kept[t["id"]]]
        print(f"{len(jobs)} haber için makale metinleri çekiliyor...")
        with ThreadPoolExecutor(8) as ex:
            grounds = list(ex.map(lambda ts: _grounding_texts(pool, ts[1]["ids"], since), jobs))
        with_text = sum(1 for texts, _ in grounds if texts)
        print(f"MAKALE METNİ BULUNAN HABER: {with_text}/{len(jobs)}")
        names, detail_jobs = [], []
        for n, ((t, s), (texts, extra)) in enumerate(zip(jobs, grounds)):
            names.append(_write_task(f"detay-{n:02d}", DETAIL_SYSTEM, detail_prompt(t, s, pool, texts)))
            detail_jobs.append({"topic": t["id"], "story": s, "n_texts": len(texts),
                                "extra": [it.to_dict() for it in extra]})
        state.update(stage="detail", jobs=detail_jobs, task_chars=state["task_chars"] + _chars(names),
                     tasks=state["tasks"] + len(names))
        _save(state)
        if names:
            _print_tasks(names)
            return
        stage = "detail"

    if stage == "detail":
        pool = _load_pool(state)
        by_id = {t["id"]: t for t in topics}
        jobs, details = [], []
        for n, j in enumerate(state["jobs"]):
            res = _read_out(f"detay-{n:02d}") or {}
            extra = [S.Item(**d) for d in j["extra"]]
            jobs.append((by_id[j["topic"]], j["story"]))
            details.append(build_detail(j["story"], res, pool, j["n_texts"], extra))
        sel = {k: (v[0], v[1]) for k, v in state["sel"].items()}
        out_topics = assemble(topics, sel, jobs, details)
        markets = state["markets"]
        started = datetime.fromisoformat(state["started"])
        bulletin = {
            "date": state["date"],
            "variant": state["variant"],
            "generated_at": datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%MZ"),
            "markets": markets,
            "market_alarms": [m for m in markets if m["alarms"]],
            "topics": out_topics,
            "stats": {"items": len(pool.items), "model": state["variant"],
                      "seconds": int((datetime.now(timezone.utc) - started).total_seconds()),
                      "tasks": state["tasks"], "task_chars": state["task_chars"]},
        }
        if state["compare"]:
            path = DATA / "compare" / f"{state['date']}-{state['variant']}.json"
        else:
            path = DATA / "bulletins" / f"{state['date']}.json"
        write_json(path, bulletin)
        state["stage"] = "done"
        _save(state)
        print(f"Kaydedildi: {path.relative_to(ROOT)} "
              f"({sum(len(t['stories']) for t in out_topics)} haber, {state['tasks']} görev, "
              f"{state['task_chars']:,} karakter girdi)")
        print("BİTTİ")
        return

    if stage == "done":
        print("BİTTİ (zaten tamamlanmış)")


def main(args: list[str]) -> None:
    if not args or args[0] not in ("prep", "next"):
        raise SystemExit(__doc__)
    if args[0] == "next":
        return next_step()
    opts = {"--variant": "claude", "--until": None, "--date": None}
    compare = False
    rest = args[1:]
    i = 0
    while i < len(rest):
        if rest[i] == "--compare":
            compare = True
            i += 1
        elif rest[i] in opts and i + 1 < len(rest):
            opts[rest[i]] = rest[i + 1]
            i += 2
        else:
            raise SystemExit(f"Bilinmeyen seçenek: {rest[i]}")
    prep(opts["--variant"], opts["--until"], opts["--date"], compare)
