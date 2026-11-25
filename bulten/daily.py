"""Günlük bülten: topla -> DeepSeek ile seç -> detaylandır -> kaydet."""
from __future__ import annotations

from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone, timedelta

from . import sources as S
from .common import TR_TZ, DATA, read_json, write_json, now_tr
from .llm import DeepSeek, estimate_cost
from .markets import fetch_markets, market_lines

ORIGIN_RANK = {"hn": 0, "hf": 0, "rss": 1, "bing": 2, "google_news": 3}

SELECT_SYSTEM = """You are the editor of a personal daily news bulletin for a Turkish reader.
You receive candidate news items for ONE topic from roughly the last 30 hours, in several languages
(Turkish, English, Spanish, Chinese). Pick only the genuinely important stories according to the topic
criteria, merge items that cover the same event, and write a one-line Turkish bullet for each.

Rules:
- Use ONLY the given items. Never add facts that are not in the titles or snippets.
- Merge items about the same event into one story and list all of their ids.
- Score importance 1-10 for THIS reader using the criteria: 10 = historic, 8 = big news, 7 = clearly worth
  knowing today, 5 = minor, 3 = noise. Be strict; most items are noise.
- Importance signals: the same event covered by many independent sources, Hacker News points,
  Hugging Face trend scores. A single clickbait or rumor source is weak evidence.
- Skip stories that were already reported on previous days (list given) unless there is a significant
  new development; in that case the headline must state what is new.
- If market data is given and a threshold is exceeded, treat that move as a story candidate even if no
  news item explains it (use ids: []).
- headline: Turkish, one sentence, at most about 25 words, concrete (who did what, numbers if present).
  No clickbait, no emojis. The criteria describe the reader only to help you choose; never put the
  reader's devices, hardware, team or other personal details into the headline or reason.
- Return at most 8 stories, most important first. An empty list is fine and expected on quiet days.

Output JSON: {"stories": [{"ids": [int], "importance": int, "headline": str, "reason": str}]}
"reason" is a short Turkish note on why it matters."""

DETAIL_SYSTEM = """You write the detailed Turkish version of ONE news story for a personal bulletin.
You get the one-line headline, the related news items (titles and snippets) and, when available,
extracted article texts that may be in English, Spanish, Chinese or Turkish.

Rules:
- Write natural, fluent Turkish. Translate foreign material.
- Use only facts in the provided material. If the material is thin, write a shorter text.
  Never invent numbers, quotes, dates or names.
- If TOPIC NOTES are given, follow them; an estimate they allow must be marked as approximate.
- title: Turkish title, at most about 12 words.
- headline: the one-line Turkish bullet (at most about 25 words). Improve it if the articles give better
  facts, otherwise keep it.
- detail: 1-3 short paragraphs separated by a blank line, at most about 120 words in total. What happened,
  key numbers and only the context needed to understand it. Write like a neutral news agency: no filler,
  no repetition of the headline or of earlier sentences, no closing summary or commentary.
- Never address or describe the reader. Do not mention any reader's devices, hardware, phone, team,
  habits or other personal details, and do not write "for the reader" style sentences.
- Refer to sources by publisher name if needed, never by bracketed item numbers like [3].

Output JSON: {"title": str, "headline": str, "detail": str}"""

DEDUPE_SYSTEM = """You get TODAY's selected stories of a daily bulletin (numbered, grouped under topics) and, when given,
the headlines that were already published on PREVIOUS DAYS (all topics).

Task 1, same event twice today: find stories that describe the SAME real-world event under different topics
(e.g. the same acquisition listed under both "AI" and "PC hardware", or the same match under a player topic and a
club topic). For each such group keep exactly one: the one whose topic fits the event best (if equal, the more
informative headline). Different events about the same company are NOT duplicates.

Task 2, repeats from previous days: the reader must not see the same news again on a later day. List a story of
today in "repeats" if it reports the same event as a previous-day headline (under any topic) and has no
significant new development. A significant new development is a new result, decision, official confirmation,
new numbers that change the picture, or a clearly new phase of the event. Rephrasing, more reactions, analysis,
or minor details are NOT new developments. When unsure, treat it as a repeat.

Headlines dated today under PREVIOUS DAYS are already in today's bulletin (an earlier run of the same day);
the same repeat rule applies to them.

Output JSON: {"groups": [{"keep": int, "drop": [int]}], "repeats": [int]} (empty lists if none)."""


def _line(it: S.Item) -> str:
    when = it.published.replace("T", " ").rstrip("Z") if it.published else "?"
    parts = [f"[{it.id}] {when}", f"{it.source} ({it.lang})"]
    if it.signal:
        parts.append(it.signal)
    text = it.title + (f" -- {it.snippet}" if it.snippet else "")
    return " | ".join(parts) + " | " + text


def collect(cfg: dict, since: datetime, pool: S.Pool, until: datetime | None = None) -> dict[str, list[list[int]]]:
    """Her başlık için, kaynak başına aday haber id listeleri döner.
    until verilirse (elle, belirli bir gün için): Google News "son 24 saat" yerine tarih aralığıyla aranır
    (after:/before:), Hacker News de aralıkla sınırlanır. RSS ve Bing sadece son haberleri verir; eski bir gün
    için bunlardan az haber gelir."""
    specs: dict[tuple, callable] = {}
    topic_specs: dict[str, list[tuple]] = {}
    feeds = cfg.get("feeds", {})
    for t in cfg["topics"]:
        src = t.get("sources", {})
        keys = []
        for name in src.get("rss", []):
            f = feeds[name]
            k = ("rss", name)
            specs[k] = lambda f=f, name=name: S.fetch_rss(f["url"], f.get("name", name), f["lang"], since)
            keys.append(k)
        for g in src.get("google_news", []):
            k = ("gn", g["q"], g["lang"])
            if until:
                rng = f' after:{(since - timedelta(days=1)):%Y-%m-%d} before:{(until + timedelta(days=1)):%Y-%m-%d}'
                specs[k] = lambda g=g, rng=rng: S.fetch_google_news(g["q"] + rng, g["lang"], since, window="",
                                                                    limit=100)
            else:
                specs[k] = lambda g=g: S.fetch_google_news(g["q"], g["lang"], since)
            keys.append(k)
        for b in src.get("bing_news", []):
            k = ("bing", b["q"], b["lang"])
            specs[k] = lambda b=b: S.fetch_bing_news(b["q"], b["lang"], since)
            keys.append(k)
        if src.get("hackernews"):
            k = ("hn",)
            specs[k] = lambda: S.fetch_hackernews(since, until=until)
            keys.append(k)
        if src.get("huggingface"):
            k = ("hf",)
            specs[k] = lambda: S.fetch_huggingface()
            keys.append(k)
        topic_specs[t["id"]] = keys

    results: dict[tuple, list[int]] = {}

    def run(k):
        try:
            items = specs[k]()
        except Exception as e:
            print(f"  kaynak hatası {k}: {type(e).__name__}: {str(e)[:120]}")
            items = []
        return k, [pool.add(it) for it in items]

    with ThreadPoolExecutor(16) as ex:
        for k, ids in ex.map(run, specs):
            results[k] = ids
            print(f"  {len(ids):4d} haber  {k}")

    return {tid: [results.get(k, []) for k in keys] for tid, keys in topic_specs.items()}


def pick_candidates(pool: S.Pool, groups: list[list[int]], limit: int, exclude: set[str]) -> list[int]:
    """Kaynaklardan sırayla (round-robin) alır ki tek bir kalabalık kaynak diğerlerini ezmesin."""
    queues = [[i for i in sorted(g, key=lambda i: pool[i].published or "", reverse=True)
               if S.norm_url(pool[i].url) not in exclude] for g in groups]
    seen, out = set(), []
    while len(out) < limit and any(queues):
        for q in queues:
            while q:
                i = q.pop(0)
                if i not in seen:
                    seen.add(i)
                    out.append(i)
                    break
            if len(out) >= limit:
                break
    return out


def select_stories(llm: DeepSeek, topic: dict, pool: S.Pool, ids: list[int],
                   prev_headlines: list[dict], markets: list[dict]) -> list[dict]:
    res = llm.json(SELECT_SYSTEM, select_prompt(topic, pool, ids, prev_headlines, markets), max_tokens=12000)
    return parse_selection(res, ids)


def select_prompt(topic: dict, pool: S.Pool, ids: list[int],
                  prev_headlines: list[dict], markets: list[dict]) -> str:
    parts = [f"TOPIC: {topic['title']}", f"CRITERIA:\n{topic['criteria'].strip()}"]
    if topic.get("market_group") and markets:
        parts.append("MARKET DATA (thresholds marked):\n" + market_lines(markets, topic["market_group"]))
    if prev_headlines:
        parts.append("ALREADY REPORTED ON PREVIOUS DAYS:\n" +
                     "\n".join(f"- {h['date']}: {h['headline']}" for h in prev_headlines[-40:]))
    parts.append("CANDIDATE ITEMS:\n" + ("\n".join(_line(pool[i]) for i in ids) or "(none)"))
    return "\n\n".join(parts)


def parse_selection(res: dict, ids: list[int]) -> list[dict]:
    valid = set(ids)
    stories = []
    for s in res.get("stories", []):
        try:
            imp = int(s.get("importance", 0))
        except (TypeError, ValueError):
            continue
        sids = [int(i) for i in s.get("ids", []) if str(i).lstrip("-").isdigit() and int(i) in valid]
        if not s.get("headline"):
            continue
        stories.append({"ids": sids, "importance": imp, "headline": s["headline"].strip(),
                        "reason": s.get("reason", "")})
    return stories


def recent_history(hist: dict, date: str, days: int) -> dict:
    """Bülten gününden önceki son `days` günün başlıkları ve kaynak adresleri.
    O günün kendisi dahil edilmez: aynı gün yeniden hazırlanırsa eski çalıştırma "önceden verilmiş" sayılmaz."""
    start = (datetime.strptime(date, "%Y-%m-%d") - timedelta(days=days)).strftime("%Y-%m-%d")
    return {"cited_urls": {u: d for u, d in hist.get("cited_urls", {}).items() if start <= d < date},
            "headlines": {k: [h for h in v if start <= h["date"] < date]
                          for k, v in hist.get("headlines", {}).items()}}


def previous_headlines(hist: dict, topics: list[dict]) -> list[list[str]]:
    """[tarih, başlık adı, haber başlığı] listesi; tüm konular, tarihe göre sıralı."""
    titles = {t["id"]: t["title"] for t in topics}
    out = [[h["date"], titles.get(tid, tid), h["headline"]]
           for tid, lst in hist.get("headlines", {}).items() for h in lst]
    return sorted(out)


def needs_dedupe(flat: list, prev: list) -> bool:
    return len(flat) >= 2 or (bool(flat) and bool(prev))


def dedupe_across_topics(llm: DeepSeek, topics: list[dict], kept: dict[str, list],
                         prev: list | None = None) -> dict[str, list]:
    """Aynı olayı iki başlıkta birden vermeyi ve önceki günlerde verilmiş haberi tekrar vermeyi önler."""
    flat = [(t, s) for t in topics for s in kept[t["id"]]]
    if not needs_dedupe(flat, prev or []):
        return kept
    try:
        res = llm.json(DEDUPE_SYSTEM, dedupe_listing(flat, prev), max_tokens=6000)
    except Exception as e:
        print("  tekilleştirme hatası:", e)
        return kept
    return apply_dedupe(res, topics, flat)


def dedupe_listing(flat: list[tuple], prev: list | None = None) -> str:
    today = "\n".join(f"[{n}] {t['title']}: {s['headline']}" for n, (t, s) in enumerate(flat))
    if not prev:
        return "TODAY:\n" + today
    return ("TODAY:\n" + today + "\n\nPREVIOUS DAYS:\n" +
            "\n".join(f"- {d} {title}: {h}" for d, title, h in prev))


def apply_dedupe(res: dict, topics: list[dict], flat: list[tuple]) -> dict[str, list]:
    drop = set()
    for g in res.get("groups", []):
        for d in g.get("drop", []):
            if isinstance(d, int) and 0 <= d < len(flat) and d != g.get("keep"):
                drop.add(d)
    for d in sorted(drop):
        print(f"  aynı gün tekrar çıkarıldı: {flat[d][0]['id']}: {flat[d][1]['headline'][:70]}")
    repeats = {d for d in res.get("repeats", []) if isinstance(d, int) and 0 <= d < len(flat)} - drop
    for d in sorted(repeats):
        print(f"  önceki günlerde verilmiş, çıkarıldı: {flat[d][0]['id']}: {flat[d][1]['headline'][:70]}")
    # Çıkarılan kopyanın başlığı, kalan habere "also" olarak eklenir: haber birden fazla başlığa ait olabilir
    for g in res.get("groups", []):
        k = g.get("keep")
        if isinstance(k, int) and 0 <= k < len(flat) and k not in drop:
            also = [flat[d][0]["id"] for d in g.get("drop", []) if d in drop and flat[d][0]["id"] != flat[k][0]["id"]]
            if also:
                flat[k][1]["also"] = sorted(set(flat[k][1].get("also", []) + also))
    drop |= repeats
    out = {t["id"]: [] for t in topics}
    for n, (t, s) in enumerate(flat):
        if n not in drop:
            out[t["id"]].append(s)
    return out


def _grounding_texts(pool: S.Pool, ids: list[int], since: datetime, max_texts: int = 3):
    """Hikayenin makale metinlerini toplar. Sadece Google News linki varsa Bing'den doğrudan link arar."""
    items = [pool[i] for i in ids]
    direct = [it for it in items if "news.google.com" not in it.url and it.origin != "hf"]
    direct.sort(key=lambda it: ORIGIN_RANK.get(it.origin, 9))
    extra = []
    if not direct and items:
        probe = sorted(items, key=lambda it: {"en": 0, "tr": 1}.get(it.lang, 2))[0]
        try:
            lang = probe.lang if probe.lang in S.LANGS else "en"
            extra = S.fetch_bing_news(probe.title[:120], lang, since - timedelta(days=1))[:3]
        except Exception:
            extra = []
        direct = extra
    texts = []
    for it in direct:
        if len(texts) >= max_texts:
            break
        txt = S.fetch_article_text(it.url)
        if len(txt) > 300:
            texts.append((it, txt))
    return texts, extra


def detail_prompt(topic: dict, story: dict, pool: S.Pool, texts: list) -> str:
    related = "\n".join(_line(pool[i]) for i in story["ids"]) or "(no news item; market data only)"
    # Konu kriteri (kişisel bilgiler içerir) bilerek verilmez: haber metnine okuyucu bilgisi sızmasın.
    parts = [f"TOPIC: {topic['title']}", f"HEADLINE: {story['headline']}", f"RELATED ITEMS:\n{related}"]
    if topic.get("detail_note"):
        parts.insert(1, f"TOPIC NOTES (follow these):\n{topic['detail_note'].strip()}")
    for n, (it, txt) in enumerate(texts, 1):
        parts.append(f"ARTICLE {n} ({it.source}, {it.lang}):\n{txt}")
    return "\n\n".join(parts)


def detail_story(llm: DeepSeek, topic: dict, story: dict, pool: S.Pool, since: datetime) -> dict:
    texts, extra = _grounding_texts(pool, story["ids"], since)
    try:
        res = llm.json(DETAIL_SYSTEM, detail_prompt(topic, story, pool, texts), max_tokens=8000, effort="none")
    except Exception as e:
        print(f"  detay hatası: {e}")
        res = {}
    return build_detail(story, res, pool, len(texts), extra)


def build_detail(story: dict, res: dict, pool: S.Pool, n_texts: int, extra: list) -> dict:
    src_items = [pool[i] for i in story["ids"]] + [it for it in extra if it.url not in {pool[i].url for i in story["ids"]}]
    return {
        "importance": story["importance"],
        "headline": (res.get("headline") or story["headline"]).strip(),
        "title": (res.get("title") or story["headline"]).strip(),
        "detail": (res.get("detail") or "").strip(),
        "sources": [{"title": it.title, "url": it.url, "source": it.source, "lang": it.lang,
                     "published": it.published} for it in src_items[:10]],
        "read_articles": n_texts,
        **({"also": story["also"]} if story.get("also") else {}),
    }


HIGH_IMPORTANCE = 9   # bu puan ve üstü seviye sınırına (max_stories) takılmaz
HIGH_EXTRA = 5        # ama bir başlığa sınırın üstünde en fazla bu kadar ek haber


def keep_stories(topics: list[dict], sel: dict, existing_counts: dict | None = None) -> dict[str, list]:
    """Eşik altını atar; aynı haberi (ortak id) iki başlıkta kullanmaz, önemi yüksek olan kalır.
    Seviye sınırı (Az 3, Orta 5, Çok 8) 9-10 puanlı haberleri kesmez: 5 tane 9 puan varsa 5'i de gelir.
    existing_counts: o günün bülteninde zaten bulunan haber sayısı (konu başına); sınır toplam üzerinden uygulanır."""
    existing_counts = existing_counts or {}
    flat = []
    for ti, t in enumerate(topics):
        st, _ = sel[t["id"]]
        for s in st or []:
            if s["importance"] >= t.get("min_importance", 7):
                flat.append((s["importance"], ti, s))
    flat.sort(key=lambda x: (-x[0], x[1]))
    used, kept = set(), {t["id"]: [] for t in topics}
    for imp, ti, s in flat:
        t = topics[ti]
        if s["ids"] and used & set(s["ids"]):
            continue
        cap = t.get("max_stories", 5)
        have = existing_counts.get(t["id"], 0) + len(kept[t["id"]])
        if have >= (cap + HIGH_EXTRA if imp >= HIGH_IMPORTANCE else cap):
            continue
        used |= set(s["ids"])
        kept[t["id"]].append(s)
    return kept


def assemble(topics: list[dict], sel: dict, jobs: list, details: list) -> list[dict]:
    by_topic: dict[str, list] = {t["id"]: [] for t in topics}
    for (t, _), d in zip(jobs, details):
        by_topic[t["id"]].append(d)
    out_topics = []
    for t in topics:
        st, n = sel[t["id"]]
        stories = sorted(by_topic[t["id"]], key=lambda d: -d["importance"])
        for k, d in enumerate(stories, 1):
            d["sid"] = f"{t['id']}-{k}"
        note = ""
        if st is None:
            note = "Bu başlık bugün işlenemedi (teknik hata)."
        elif not stories:
            note = "Kayda değer bir gelişme yok."
        out_topics.append({"id": t["id"], "title": t["title"], "stories": stories,
                           "note": note, "candidates": n})
    return out_topics


def update_history(out_topics: list[dict], date: str) -> None:
    """Son 7 günün başlıklarını ve kaynak adreslerini saklar (sonraki günlerde tekrar gelmesin).
    Aynı gün yeniden hazırlanırsa o günün eski kayıtları silinip yenisi yazılır."""
    hist_path = DATA / "history.json"
    hist = read_json(hist_path, {"cited_urls": {}, "headlines": {}})
    keep_after = (now_tr() - timedelta(days=7)).strftime("%Y-%m-%d")
    hist["cited_urls"] = {u: d for u, d in hist["cited_urls"].items() if d != date}
    for k in hist["headlines"]:
        hist["headlines"][k] = [h for h in hist["headlines"][k] if h["date"] != date]
    for t in out_topics:
        lst = hist["headlines"].setdefault(t["id"], [])
        lst.extend({"date": date, "headline": s["headline"]} for s in t["stories"])
        hist["headlines"][t["id"]] = sorted((h for h in lst if h["date"] >= keep_after),
                                            key=lambda h: h["date"])[-60:]
        for s in t["stories"]:
            for src in s["sources"]:
                hist["cited_urls"][S.norm_url(src["url"])] = date
    hist["cited_urls"] = {u: d for u, d in hist["cited_urls"].items() if d >= keep_after}
    write_json(hist_path, hist)


def existing_bulletin(date: str) -> dict | None:
    """O günün bülteni zaten yayınlandıysa (ör. 21:00 rutini) onu döner."""
    return read_json(DATA / "bulletins" / f"{date}.json")


def with_existing(hist: dict, existing: dict | None, date: str) -> dict:
    """recent_history çıktısına o günün mevcut bülteninin haberlerini ekler: kaynak adresleri aday olmasın,
    başlıkları "bugünkü bültende zaten var" diye tekrar kontrolüne girsin."""
    if not existing:
        return hist
    out = {"cited_urls": dict(hist["cited_urls"]), "headlines": {k: list(v) for k, v in hist["headlines"].items()}}
    for t in existing.get("topics", []):
        for s in t.get("stories", []):
            out["headlines"].setdefault(t["id"], []).append({"date": date, "headline": s["headline"]})
            for src in s.get("sources", []):
                out["cited_urls"][S.norm_url(src["url"])] = date
    return out


def existing_counts(existing: dict | None) -> dict[str, int]:
    return {t["id"]: len(t.get("stories", [])) for t in (existing or {}).get("topics", [])}


_SUM_KEYS = ("items", "seconds", "calls", "prompt_tokens", "completion_tokens", "cache_hit_tokens",
             "tasks", "task_chars")


def _run_of(b: dict) -> dict:
    st = b.get("stats", {})
    run = {"at": b.get("generated_at"), "model": st.get("model"), "items": st.get("items", 0)}
    if "cost_usd" in st:
        run["cost_usd"] = st["cost_usd"]
    return run


def merge_bulletin(old: dict, new: dict) -> dict:
    """Aynı günün ikinci çalıştırması: eski haberler AYNEN kalır (sid değişmez, uygulamadaki okundu bilgisi
    "tarih/sid" ile tutulur), yeni haberler eklenir (added_at ile). generated_at eskisi kalır, updated_at yenisi olur."""
    run_at = new["generated_at"]
    by_new = {t["id"]: t for t in new["topics"]}
    by_old = {t["id"]: t for t in old["topics"]}
    order = list(by_old) + [tid for tid in by_new if tid not in by_old]
    topics = []
    for tid in order:
        o, n = by_old.get(tid), by_new.get(tid)
        if o is None:   # eski bültende olmayan başlık: olduğu gibi eklenir, haberleri yeni sayılır
            topics.append({**n, "stories": [{**s, "added_at": run_at} for s in n["stories"]]})
            continue
        stories = [dict(s) for s in o["stories"]]
        last = 0
        for s in stories:
            tail = str(s.get("sid", "")).rsplit("-", 1)[-1]
            if tail.isdigit():
                last = max(last, int(tail))
        for k, s in enumerate((n or {}).get("stories", []), 1):
            s = dict(s)
            s["sid"] = f"{tid}-{last + k}"
            s["added_at"] = run_at
            stories.append(s)
        stories.sort(key=lambda s: -s["importance"])   # kararlı: eşitlikte eski haber önce
        note = "" if stories else ((n or {}).get("note") or o.get("note", ""))
        topics.append({"id": tid, "title": (n or o)["title"], "stories": stories, "note": note,
                       "candidates": o.get("candidates", 0) + (n or {}).get("candidates", 0)})
    ost, nst = old.get("stats", {}), new.get("stats", {})
    stats = dict(nst)   # model: son çalıştırmanınki
    for k in _SUM_KEYS:
        if k in ost or k in nst:
            stats[k] = ost.get(k, 0) + nst.get(k, 0)
    runs = list(ost.get("runs") or [_run_of(old)]) + [_run_of(new)]
    costs = [r["cost_usd"] for r in runs if "cost_usd" in r]
    if costs:
        stats["cost_usd"] = round(sum(costs), 4)
    else:
        stats.pop("cost_usd", None)
    stats["runs"] = runs
    if new.get("markets"):
        markets, alarms = new["markets"], [m for m in new["markets"] if m.get("alarms")]
    else:
        markets, alarms = old.get("markets", []), old.get("market_alarms", [])
    return {**old, "topics": topics, "markets": markets, "market_alarms": alarms,
            "updated_at": run_at, "stats": stats}


def merge_with_existing(date: str, bulletin: dict) -> dict:
    """O günün bülteni varsa yeni çalıştırmayı onunla birleştirir, yoksa olduğu gibi döner."""
    old = existing_bulletin(date)
    return merge_bulletin(old, bulletin) if old else bulletin


def run_daily(cfg: dict, date: str | None = None) -> dict:
    """Elle bülten (DeepSeek). Hangi gün çalıştırılırsa çalıştırılsın kapsam tarihe bağlıdır:
    - date verilirse: sadece o gün (TR 00:00 - 24:00; bugünse şu ana kadar). Bülten o günün tarihini alır.
    - date yoksa: dün ve bugün birlikte (dün TR 00:00'dan şu ana kadar). Bülten bugünün tarihini alır."""
    started = datetime.now(timezone.utc)
    today = now_tr().date()
    if date:
        day = datetime.strptime(date, "%Y-%m-%d").date()
        since = datetime(day.year, day.month, day.day, tzinfo=TR_TZ).astimezone(timezone.utc)
        end = min(started, since + timedelta(days=1))
    else:
        date = today.isoformat()
        y = today - timedelta(days=1)
        since = datetime(y.year, y.month, y.day, tzinfo=TR_TZ).astimezone(timezone.utc)
        end = started
    print(f"Kapsam: {since:%Y-%m-%d %H:%M}Z - {end:%Y-%m-%d %H:%M}Z, bülten günü {date}")
    hist_path = DATA / "history.json"
    hist = recent_history(read_json(hist_path, {"cited_urls": {}, "headlines": {}}), date,
                          cfg.get("repeat_days", 5))
    existing = existing_bulletin(date)   # o gün bülten zaten varsa yenisi onunla birleştirilir
    if existing:
        print(f"{date} bülteni zaten var: sadece yeni haberler eklenecek")
    hist = with_existing(hist, existing, date)
    cited = set(hist["cited_urls"])

    print("Kaynaklar taranıyor...")
    pool = S.Pool()
    topic_ids = collect(cfg, since, pool, until=end)
    if end < started:
        cutoff = end.strftime("%Y-%m-%dT%H:%M")
        late = {it.id for it in pool.items if it.published and it.published[:16] > cutoff}
        topic_ids = {k: [[i for i in g if i not in late] for g in groups] for k, groups in topic_ids.items()}
        print(f"{date} bülteni: kesim saatinden ({cutoff}Z) sonraki {len(late)} haber çıkarıldı")
    print(f"Toplam tekil haber: {len(pool.items)}")

    print("Piyasa verisi alınıyor...")
    try:
        markets = fetch_markets(cfg.get("markets", []))
    except Exception as e:
        print("Piyasa verisi alınamadı:", e)
        markets = []

    llm = DeepSeek(cfg.get("model", "deepseek-flash"))
    limit = cfg.get("max_candidates_per_topic", 220)
    topics = cfg["topics"]

    def stage1(t):
        ids = pick_candidates(pool, topic_ids[t["id"]], limit, cited)
        try:
            st = select_stories(llm, t, pool, ids, hist["headlines"].get(t["id"], []), markets)
        except Exception as e:
            print(f"  seçim hatası {t['id']}: {e}")
            return t["id"], None, len(ids)
        return t["id"], st, len(ids)

    print("DeepSeek haberleri seçiyor...")
    with ThreadPoolExecutor(6) as ex:
        sel = {tid: (st, n) for tid, st, n in ex.map(stage1, topics)}

    kept = dedupe_across_topics(llm, topics, keep_stories(topics, sel, existing_counts(existing)),
                               previous_headlines(hist, topics))

    print("Detaylar yazılıyor...")
    jobs = [(t, s) for t in topics for s in kept[t["id"]]]
    with ThreadPoolExecutor(6) as ex:
        details = list(ex.map(lambda ts: detail_story(llm, ts[0], ts[1], pool, since), jobs))
    out_topics = assemble(topics, sel, jobs, details)

    alarms = [m for m in markets if m["alarms"]]
    bulletin = {
        "date": date,
        "generated_at": datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%MZ"),
        "markets": markets,
        "market_alarms": alarms,
        "topics": out_topics,
        "stats": {"items": len(pool.items), "seconds": int((datetime.now(timezone.utc) - started).total_seconds()),
                  "model": llm.model, **llm.usage, "cost_usd": estimate_cost(llm.usage, started)},
    }
    print(f"Bu çalıştırma: {sum(len(t['stories']) for t in out_topics)} haber, kullanım: {llm.usage}, "
          f"tahmini maliyet: ${bulletin['stats']['cost_usd']}")
    if existing:
        bulletin = merge_bulletin(existing, bulletin)
    write_json(DATA / "bulletins" / f"{date}.json", bulletin)
    update_history(bulletin["topics"], date)
    print(f"Bitti: bültende toplam {sum(len(t['stories']) for t in bulletin['topics'])} haber")
    return bulletin
