"""Haber kaynaklarından aday haber toplama. Burada yapay zeka yok, sadece veri çekme."""
from __future__ import annotations

import calendar
import html
import re
import threading
import urllib.parse
from dataclasses import dataclass, asdict
from datetime import datetime, timezone, timedelta

import feedparser
import requests

UA = ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
      "(KHTML, like Gecko) Chrome/131.0 Safari/537.36")

_local = threading.local()


def session() -> requests.Session:
    s = getattr(_local, "s", None)
    if s is None:
        s = requests.Session()
        s.headers["User-Agent"] = UA
        _local.s = s
    return s


# dil -> (Google News hl, gl, ceid, Bing mkt)
LANGS = {
    "tr": ("tr", "TR", "TR:tr", "tr-TR"),
    "en": ("en-US", "US", "US:en", "en-US"),
    "es": ("es", "ES", "ES:es", "es-ES"),
    "zh": ("zh-CN", "CN", "CN:zh-Hans", "zh-CN"),
}


@dataclass
class Item:
    title: str
    url: str
    source: str
    lang: str
    published: str          # ISO, UTC
    snippet: str = ""
    signal: str = ""        # ör. "HN 450 puan"
    origin: str = ""        # rss / google_news / bing / hn / hf
    id: int = -1

    def to_dict(self):
        return asdict(self)


_TAG = re.compile(r"<[^>]+>")
_WS = re.compile(r"\s+")


def clean(s: str | None, limit: int = 300) -> str:
    s = html.unescape(_TAG.sub(" ", s or ""))
    s = _WS.sub(" ", s).strip()
    return s if len(s) <= limit else s[:limit].rsplit(" ", 1)[0] + "..."


def _entry_time(e) -> datetime | None:
    t = e.get("published_parsed") or e.get("updated_parsed")
    if not t:
        return None
    return datetime.fromtimestamp(calendar.timegm(t), tz=timezone.utc)


def _iso(d: datetime) -> str:
    return d.astimezone(timezone.utc).strftime("%Y-%m-%dT%H:%MZ")


def _get_feed(url: str):
    r = session().get(url, timeout=25)
    r.raise_for_status()
    return feedparser.parse(r.content).entries


def fetch_rss(url: str, name: str, lang: str, since: datetime) -> list[Item]:
    out = []
    for e in _get_feed(url):
        t = _entry_time(e)
        if t and t < since:
            continue
        out.append(Item(
            title=clean(e.get("title"), 250), url=e.get("link", ""), source=name, lang=lang,
            published=_iso(t) if t else "", snippet=clean(e.get("summary"), 300), origin="rss"))
    return out


def google_news_url(q: str, lang: str, window: str = "1d") -> str:
    hl, gl, ceid, _ = LANGS[lang]
    q = f"{q} when:{window}" if window else q
    return (f"https://news.google.com/rss/search?q={urllib.parse.quote(q)}"
            f"&hl={hl}&gl={gl}&ceid={urllib.parse.quote(ceid)}")


def fetch_google_news(q: str, lang: str, since: datetime, window: str = "1d", limit: int = 60) -> list[Item]:
    out = []
    for e in _get_feed(google_news_url(q, lang, window))[:limit]:
        t = _entry_time(e)
        if t and t < since:
            continue
        title = clean(e.get("title"), 250)
        src = (e.get("source") or {}).get("title") or ""
        if src and title.endswith(" - " + src):
            title = title[: -len(src) - 3]
        out.append(Item(title=title, url=e.get("link", ""), source=src or "Google News", lang=lang,
                        published=_iso(t) if t else "", origin="google_news"))
    return out


def _bing_real_url(link: str) -> str:
    qs = urllib.parse.parse_qs(urllib.parse.urlparse(link).query)
    return qs.get("url", [link])[0]


def fetch_bing_news(q: str, lang: str, since: datetime) -> list[Item]:
    mkt = LANGS[lang][3]
    url = f"https://www.bing.com/news/search?q={urllib.parse.quote(q)}&format=rss&mkt={mkt}"
    out = []
    for e in _get_feed(url):
        t = _entry_time(e)
        if t and t < since:
            continue
        real = _bing_real_url(e.get("link", ""))
        src = e.get("news_source") or urllib.parse.urlparse(real).netloc.replace("www.", "")
        out.append(Item(title=clean(e.get("title"), 250), url=real, source=src, lang=lang,
                        published=_iso(t) if t else "", snippet=clean(e.get("summary"), 300), origin="bing"))
    return out


def fetch_hackernews(since: datetime, min_points: int = 120, query: str = "",
                     until: datetime | None = None) -> list[Item]:
    flt = f"created_at_i>{int(since.timestamp())},points>{min_points}"
    if until:
        flt += f",created_at_i<{int(until.timestamp())}"
    params = {"tags": "story", "hitsPerPage": 100, "numericFilters": flt}
    endpoint = "search" if query else "search_by_date"
    if query:
        params["query"] = query
    r = session().get(f"https://hn.algolia.com/api/v1/{endpoint}", params=params, timeout=25)
    r.raise_for_status()
    out = []
    for h in r.json().get("hits", []):
        url = h.get("url") or f"https://news.ycombinator.com/item?id={h['objectID']}"
        t = datetime.fromtimestamp(h["created_at_i"], tz=timezone.utc)
        out.append(Item(title=clean(h.get("title"), 250), url=url,
                        source=urllib.parse.urlparse(url).netloc.replace("www.", "") or "Hacker News",
                        lang="en", published=_iso(t),
                        signal=f"Hacker News {h.get('points', 0)} puan, {h.get('num_comments', 0)} yorum",
                        origin="hn"))
    return out


def fetch_huggingface(max_age_days: int = 10, limit: int = 40) -> list[Item]:
    r = session().get("https://huggingface.co/api/models",
                      params={"sort": "trendingScore", "limit": limit}, timeout=25)
    r.raise_for_status()
    cutoff = datetime.now(timezone.utc) - timedelta(days=max_age_days)
    out = []
    for m in r.json():
        created = m.get("createdAt")
        t = datetime.fromisoformat(created.replace("Z", "+00:00")) if created else None
        if t and t < cutoff:
            continue
        mid = m["id"]
        out.append(Item(
            title=f"Hugging Face trend modeli: {mid}", url=f"https://huggingface.co/{mid}",
            source="Hugging Face", lang="en", published=_iso(t) if t else "",
            snippet=f"tür: {m.get('pipeline_tag') or 'bilinmiyor'}",
            signal=f"HF trend puanı {m.get('trendingScore')}, {m.get('likes', 0)} beğeni",
            origin="hf"))
    return out


def fetch_article_text(url: str, limit: int = 6000) -> str:
    """Makale metnini çıkarır. Başarısız olursa boş döner."""
    if not url or "news.google.com" in url:
        return ""
    try:
        import trafilatura
        r = session().get(url, timeout=20)
        if r.status_code != 200 or "html" not in r.headers.get("Content-Type", "html"):
            return ""
        text = trafilatura.extract(r.text, include_comments=False, include_tables=False) or ""
        return text[:limit]
    except Exception:
        return ""


_TRACK = re.compile(r"^(utm_|fbclid|gclid|ref$|src$|cmpid|ocid)")


def norm_url(url: str) -> str:
    p = urllib.parse.urlparse(url)
    q = [(k, v) for k, v in urllib.parse.parse_qsl(p.query) if not _TRACK.match(k)]
    return urllib.parse.urlunparse((p.scheme.lower(), p.netloc.lower().replace("www.", ""),
                                    p.path.rstrip("/"), "", urllib.parse.urlencode(q), ""))


def norm_title(t: str) -> str:
    return re.sub(r"\W+", "", t.lower())[:80]


class Pool:
    """Tüm başlıklar için ortak, tekrarsız haber havuzu."""

    def __init__(self):
        self.items: list[Item] = []
        self._by_url: dict[str, int] = {}
        self._by_title: dict[str, int] = {}
        self._lock = threading.Lock()

    def add(self, it: Item) -> int:
        with self._lock:
            u, t = norm_url(it.url), norm_title(it.title)
            if u in self._by_url:
                return self._by_url[u]
            if t and t in self._by_title:
                existing = self.items[self._by_title[t]]
                if it.signal and not existing.signal:
                    existing.signal = it.signal
                return existing.id
            it.id = len(self.items)
            self.items.append(it)
            self._by_url[u] = it.id
            if t:
                self._by_title[t] = it.id
            return it.id

    def __getitem__(self, i: int) -> Item:
        return self.items[i]
