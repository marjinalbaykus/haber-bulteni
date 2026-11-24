"""Günlük bülten: topla -> DeepSeek ile seç -> detaylandır -> kaydet."""
from __future__ import annotations

from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone, timedelta

from . import sources as S
from .common import DATA, read_json, write_json, now_tr
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
  No clickbait, no emojis.
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
- title: Turkish title, at most about 12 words.
- headline: the one-line Turkish bullet (at most about 25 words). Improve it if the articles give better
  facts, otherwise keep it.
- detail: 2-4 short paragraphs separated by a blank line. What happened, key numbers, context, and why
  it matters for this reader (topic criteria given).
- key_points: 2-5 short Turkish bullet points.
- Refer to sources by publisher name if needed, never by bracketed item numbers like [3].

Output JSON: {"title": str, "headline": str, "detail": str, "key_points": [str]}"""

DEDUPE_SYSTEM = """You get the selected stories of a daily bulletin, grouped under topics.
Find stories that describe the SAME real-world event under different topics (e.g. the same acquisition
listed under both "AI" and "PC hardware"). For each such group keep exactly one: the one whose topic fits
the event best (if equal, the more informative headline). Different events about the same company are NOT
duplicates.
Output JSON: {"groups": [{"keep": int, "drop": [int]}]} (empty list if there are no duplicates)."""


def _line(it: S.Item) -> str:
    when = it.published.replace("T", " ").rstrip("Z") if it.published else "?"
    parts = [f"[{it.id}] {when}", f"{it.source} ({it.lang})"]
    if it.signal:
        parts.append(it.signal)
    text = it.title + (f" -- {it.snippet}" if it.snippet else "")
    return " | ".join(parts) + " | " + text


def collect(cfg: dict, since: datetime, pool: S.Pool) -> dict[str, list[list[int]]]:
    """Her başlık için, kaynak başına aday haber id listeleri döner."""
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
            specs[k] = lambda g=g: S.fetch_google_news(g["q"], g["lang"], since)
            keys.append(k)
        for b in src.get("bing_news", []):
            k = ("bing", b["q"], b["lang"])
            specs[k] = lambda b=b: S.fetch_bing_news(b["q"], b["lang"], since)
            keys.append(k)
        if src.get("hackernews"):
            k = ("hn",)
            specs[k] = lambda: S.fetch_hackernews(since)
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
                     "\n".join(f"- {h['date']}: {h['headline']}" for h in prev_headlines[-25:]))
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


def dedupe_across_topics(llm: DeepSeek, topics: list[dict], kept: dict[str, list]) -> dict[str, list]:
    """Farklı makalelerle iki başlıkta birden çıkan aynı olayı tekilleştirir."""
    flat = [(t, s) for t in topics for s in kept[t["id"]]]
    if len(flat) < 2:
        return kept
    try:
        res = llm.json(DEDUPE_SYSTEM, dedupe_listing(flat), max_tokens=6000)
    except Exception as e:
        print("  tekilleştirme hatası:", e)
        return kept
    return apply_dedupe(res, topics, flat)


def dedupe_listing(flat: list[tuple]) -> str:
    return "\n".join(f"[{n}] {t['title']}: {s['headline']}" for n, (t, s) in enumerate(flat))


def apply_dedupe(res: dict, topics: list[dict], flat: list[tuple]) -> dict[str, list]:
    drop = set()
    for g in res.get("groups", []):
        for d in g.get("drop", []):
            if isinstance(d, int) and 0 <= d < len(flat) and d != g.get("keep"):
                drop.add(d)
    for d in sorted(drop):
        print(f"  tekrar çıkarıldı: {flat[d][0]['id']}: {flat[d][1]['headline'][:70]}")
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
    parts = [f"TOPIC: {topic['title']}", f"CRITERIA:\n{topic['criteria'].strip()}",
             f"HEADLINE: {story['headline']}", f"RELATED ITEMS:\n{related}"]
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
        "key_points": [p for p in res.get("key_points", []) if isinstance(p, str)][:6],
        "sources": [{"title": it.title, "url": it.url, "source": it.source, "lang": it.lang,
                     "published": it.published} for it in src_items[:10]],
        "read_articles": n_texts,
    }


def keep_stories(topics: list[dict], sel: dict) -> dict[str, list]:
    """Eşik altını atar; aynı haberi (ortak id) iki başlıkta kullanmaz, önemi yüksek olan kalır."""
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
        if len(kept[t["id"]]) >= t.get("max_stories", 5):
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
    """Son 7 günün başlıklarını ve kaynak adreslerini saklar (ertesi gün tekrar gelmesin)."""
    hist_path = DATA / "history.json"
    hist = read_json(hist_path, {"cited_urls": {}, "headlines": {}})
    keep_after = (now_tr() - timedelta(days=7)).strftime("%Y-%m-%d")
    for t in out_topics:
        lst = hist["headlines"].setdefault(t["id"], [])
        lst.extend({"date": date, "headline": s["headline"]} for s in t["stories"])
        hist["headlines"][t["id"]] = [h for h in lst if h["date"] >= keep_after][-40:]
        for s in t["stories"]:
            for src in s["sources"]:
                hist["cited_urls"][S.norm_url(src["url"])] = date
    hist["cited_urls"] = {u: d for u, d in hist["cited_urls"].items() if d >= keep_after}
    write_json(hist_path, hist)


def run_daily(cfg: dict) -> dict:
    started = datetime.now(timezone.utc)
    since = started - timedelta(hours=cfg.get("lookback_hours", 30))
    date = now_tr().strftime("%Y-%m-%d")
    hist_path = DATA / "history.json"
    hist = read_json(hist_path, {"cited_urls": {}, "headlines": {}})
    cited = set(hist["cited_urls"])

    print("Kaynaklar taranıyor...")
    pool = S.Pool()
    topic_ids = collect(cfg, since, pool)
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

    kept = dedupe_across_topics(llm, topics, keep_stories(topics, sel))

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
    write_json(DATA / "bulletins" / f"{date}.json", bulletin)
    update_history(out_topics, date)
    print(f"Bitti: {sum(len(t['stories']) for t in out_topics)} haber, kullanım: {llm.usage}, "
          f"tahmini maliyet: ${bulletin['stats']['cost_usd']}")
    return bulletin
