"""İsteğe bağlı araştırma: 'araştır <konu>' komutu."""
from __future__ import annotations

import re
import unicodedata
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone, timedelta

from . import sources as S
from .common import DATA, write_json, now_tr
from .llm import DeepSeek, estimate_cost

PLAN_SYSTEM = """A Turkish user asks you to research a topic using news and web sources.
Produce search queries for news search engines.
- title: short Turkish title for the research (max 8 words).
- queries: per language lists. "en": 1-3 queries (always), "tr": 0-2, "zh": 0-2 only if Chinese sources are
  likely useful (tech, AI, hardware, China), "es": 0-1 only for Spanish football or Spain.
  Queries must be short keyword queries, not sentences.
- days: how far back to search: 3 for breaking events, 30 by default, 365 for broad or slow topics.
- hn_query: 1-4 English keywords for Hacker News search, or "" if not a tech topic.
Output JSON: {"title": str, "queries": {"en": [], "tr": [], "zh": [], "es": []}, "days": int, "hn_query": str}"""

SELECT_SYSTEM = """Pick the news items most relevant and informative for the research question.
Prefer recent, substantive, primary or reputable sources; avoid duplicates and clickbait.
Return at most 15 ids, best first. Output JSON: {"ids": [int]}"""

REPORT_SYSTEM = """Write a research report in natural, fluent Turkish for a Turkish reader, answering their
research request. Use ONLY the provided items and article texts (translate foreign material).
Never invent facts, numbers, dates or quotes. If the material is insufficient or contradictory, say so.
- title: Turkish title (max 10 words).
- summary_points: 3-6 short Turkish bullets with the most important findings.
- sections: 2-5 sections, each {"heading": str, "body": str}; body is 1-3 short paragraphs separated by a
  blank line. When useful, mention which source a key claim comes from BY PUBLISHER NAME
  (e.g. "Tom's Hardware'a göre"). Never write bracketed item numbers like [3] in the text.
- source_ids: ids of the items you actually used.
- caveats: one Turkish sentence on limitations (e.g. few sources, rumors), or "".
Output JSON: {"title": str, "summary_points": [str], "sections": [{"heading": str, "body": str}],
"source_ids": [int], "caveats": str}"""


def slug(s: str) -> str:
    s = unicodedata.normalize("NFKD", s.replace("ı", "i")).encode("ascii", "ignore").decode()
    return re.sub(r"[^a-z0-9]+", "-", s.lower()).strip("-")[:40] or "arastirma"


def _window(days: int) -> str:
    return f"{days}d" if days <= 30 else "1y"


def run_research(cfg: dict, query: str) -> dict:
    llm = DeepSeek(cfg.get("model", "deepseek-flash"))
    plan = llm.json(PLAN_SYSTEM, f"Research request: {query}", max_tokens=4000)
    days = max(1, min(int(plan.get("days") or 30), 365))
    since = datetime.now(timezone.utc) - timedelta(days=days)
    print("Plan:", plan)

    jobs = []
    for lang, qs in (plan.get("queries") or {}).items():
        if lang not in S.LANGS:
            continue
        for q in (qs or [])[:3]:
            jobs.append(lambda q=q, lang=lang: S.fetch_google_news(q, lang, since, window=_window(days), limit=40))
            if lang in ("en", "tr"):
                jobs.append(lambda q=q, lang=lang: S.fetch_bing_news(q, lang, since))
    if plan.get("hn_query"):
        jobs.append(lambda: S.fetch_hackernews(since, min_points=20, query=plan["hn_query"]))

    pool = S.Pool()

    def run(fn):
        try:
            return fn()
        except Exception as e:
            print("  kaynak hatası:", e)
            return []

    with ThreadPoolExecutor(10) as ex:
        for items in ex.map(run, jobs):
            for it in items:
                pool.add(it)
    cands = pool.items[:200]
    print(f"Aday haber: {len(cands)}")

    from .daily import _line
    listing = "\n".join(_line(it) for it in cands) or "(none)"
    sel = llm.json(SELECT_SYSTEM, f"RESEARCH REQUEST: {query}\n\nITEMS:\n{listing}", max_tokens=6000)
    valid = {it.id for it in cands}
    ids = [int(i) for i in sel.get("ids", []) if str(i).isdigit() and int(i) in valid][:15]

    def text(it):
        return it, S.fetch_article_text(it.url, limit=5000)

    direct = [pool[i] for i in ids if "news.google.com" not in pool[i].url][:8]
    with ThreadPoolExecutor(8) as ex:
        texts = [(it, t) for it, t in ex.map(text, direct) if len(t) > 300][:6]

    parts = [f"RESEARCH REQUEST: {query}", "ITEMS:\n" + "\n".join(_line(pool[i]) for i in ids)]
    for it, t in texts:
        parts.append(f"ARTICLE [{it.id}] ({it.source}, {it.lang}):\n{t}")
    rep = llm.json(REPORT_SYSTEM, "\n\n".join(parts), max_tokens=16000)

    used = [int(i) for i in rep.get("source_ids", []) if str(i).isdigit() and int(i) in valid] or ids
    now = now_tr()
    rid = f"{now:%Y%m%d-%H%M}-{slug(query)}"
    result = {
        "id": rid,
        "query": query,
        "title": rep.get("title") or plan.get("title") or query,
        "created": now.strftime("%Y-%m-%d %H:%M"),
        "summary_points": [p for p in rep.get("summary_points", []) if isinstance(p, str)],
        "sections": [s for s in rep.get("sections", []) if isinstance(s, dict)],
        "caveats": rep.get("caveats", ""),
        "sources": [{"title": pool[i].title, "url": pool[i].url, "source": pool[i].source,
                     "lang": pool[i].lang, "published": pool[i].published} for i in used[:15]],
        "stats": {"candidates": len(cands), "read_articles": len(texts), "model": llm.model, **llm.usage,
                  "cost_usd": estimate_cost(llm.usage)},
    }
    write_json(DATA / "research" / f"{rid}.json", result)
    print("Araştırma bitti:", rid, llm.usage)
    return result
