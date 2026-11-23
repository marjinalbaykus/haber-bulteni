"""data/ altındaki JSON'lardan docs/ altına statik site üretir (GitHub Pages)."""
from __future__ import annotations

import html
import json
from pathlib import Path

from .common import DATA, DOCS, tr_date, pct_text, utc_to_tr

LANG_LABEL = {"tr": "TR", "en": "EN", "es": "ES", "zh": "ZH"}
WIN_LABEL = {"day": "1 gün", "week": "7 gün", "month": "30 gün"}

CSS = """
:root{--bg:#f6f6f3;--card:#fff;--fg:#1d1d1b;--muted:#6b6b66;--line:#e3e2dc;--accent:#1f5fbf;--hl:#fff6d6;--warn:#b3261e}
@media (prefers-color-scheme:dark){:root{--bg:#121212;--card:#1d1d1d;--fg:#ececea;--muted:#9a9a95;--line:#2e2e2c;--accent:#7fb0ff;--hl:#2d2a1c;--warn:#ff8a80}}
*{box-sizing:border-box}
body{margin:0;background:var(--bg);color:var(--fg);font:16px/1.55 system-ui,-apple-system,"Segoe UI",Roboto,sans-serif;-webkit-text-size-adjust:100%}
main{max-width:760px;margin:0 auto;padding:16px 16px 64px}
a{color:var(--accent);text-decoration:none}
h1{font-size:1.45rem;margin:.4rem 0 .2rem}
h2{font-size:1.1rem;margin:1.6rem 0 .5rem;padding-bottom:.3rem;border-bottom:1px solid var(--line)}
h3{font-size:1.05rem;margin:.1rem 0 .4rem;line-height:1.35}
.muted{color:var(--muted);font-size:.88rem}
nav.top{display:flex;justify-content:space-between;gap:8px;font-size:.92rem;margin-bottom:6px}
ul.sum{padding-left:1.1rem;margin:.3rem 0}
ul.sum li{margin:.35rem 0}
.empty{color:var(--muted);font-style:italic;margin:.3rem 0}
.card{background:var(--card);border:1px solid var(--line);border-radius:12px;padding:14px 15px;margin:12px 0;scroll-margin-top:12px}
.card:target{background:var(--hl);border-color:var(--accent)}
.tag{display:inline-block;font-size:.75rem;color:var(--muted);border:1px solid var(--line);border-radius:999px;padding:1px 8px;margin-right:6px}
.card p{margin:.55rem 0}
.kp{margin:.5rem 0;padding-left:1.1rem}
details{margin-top:.6rem}
summary{cursor:pointer;color:var(--accent);font-size:.92rem}
.src{list-style:none;padding:0;margin:.4rem 0 0}
.src li{margin:.35rem 0;font-size:.92rem;line-height:1.35}
.src .l{font-size:.72rem;color:var(--muted);border:1px solid var(--line);border-radius:4px;padding:0 4px;margin-right:4px}
table{width:100%;border-collapse:collapse;font-size:.88rem}
td,th{padding:6px 4px;border-bottom:1px solid var(--line);text-align:right}
td:first-child,th:first-child{text-align:left}
.alarm{color:var(--warn);font-weight:600}
.alarmbox{border:1px solid var(--warn);border-radius:12px;padding:10px 14px;margin:12px 0}
.list a{display:block;padding:10px 0;border-bottom:1px solid var(--line)}
"""


def e(s) -> str:
    return html.escape(str(s or ""))


def paras(text: str) -> str:
    return "".join(f"<p>{e(p.strip())}</p>" for p in (text or "").split("\n\n") if p.strip())


def page(title: str, body: str, depth: int) -> str:
    up = "../" * depth
    return f"""<!doctype html><html lang="tr"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>{e(title)}</title><link rel="stylesheet" href="{up}style.css">
<link rel="manifest" href="{up}manifest.json"><meta name="theme-color" content="#1f5fbf">
</head><body><main>{body}</main></body></html>"""


def sources_html(srcs: list[dict]) -> str:
    items = "".join(
        f'<li><span class="l">{LANG_LABEL.get(s.get("lang"), "?")}</span>'
        f'<a href="{e(s["url"])}" target="_blank" rel="noopener">{e(s["title"])}</a>'
        f' <span class="muted">{e(s.get("source"))}</span></li>' for s in srcs)
    return f'<details><summary>Kaynaklar ({len(srcs)})</summary><ul class="src">{items}</ul></details>' if srcs else ""


def market_table(markets: list[dict]) -> str:
    if not markets:
        return ""
    rows = []
    for m in markets:
        alarmed = {a["window"] for a in m["alarms"]}
        cells = "".join(
            f'<td class="{"alarm" if k in alarmed else ""}">'
            f'{"%+.1f" % m["changes"][k] if k in m["changes"] else "-"}%</td>' for k in WIN_LABEL)
        rows.append(f"<tr><td>{e(m['name'])}</td><td>{m['value']:,}</td>{cells}</tr>")
    head = "".join(f"<th>{v}</th>" for v in WIN_LABEL.values())
    return (f'<details><summary>Piyasa tablosu</summary><table><tr><th>Varlık</th><th>Değer</th>{head}</tr>'
            f'{"".join(rows)}</table><p class="muted">Kırmızı: alarm eşiği aşıldı. Eşikler config.yaml içinde.</p></details>')


def render_bulletin(b: dict) -> str:
    d = b["date"]
    out = [f'<nav class="top"><a href="../index.html">Tüm bültenler</a><span class="muted">{e(utc_to_tr(b["generated_at"]))}</span></nav>',
           f'<h1>Günün Özeti</h1><div class="muted">{e(tr_date(d))}</div>']
    if b.get("market_alarms"):
        lines = "".join(
            f"<li><b>{e(m['name'])}</b>: " + ", ".join(f"{a['label']} {pct_text(a['pct'])}" for a in m["alarms"]) + "</li>"
            for m in b["market_alarms"])
        out.append(f'<div class="alarmbox"><b>Piyasa alarmı</b><ul class="sum">{lines}</ul></div>')
    # Özet
    out.append("<h2>Kısa özet</h2>")
    for t in b["topics"]:
        out.append(f"<h3>{e(t['title'])}</h3>")
        if t["stories"]:
            out.append('<ul class="sum">' + "".join(
                f'<li><a href="#{s["sid"]}">{e(s["headline"])}</a></li>' for s in t["stories"]) + "</ul>")
        else:
            out.append(f'<p class="empty">{e(t["note"])}</p>')
    # Detaylar
    out.append("<h2>Detaylar</h2>")
    for t in b["topics"]:
        for s in t["stories"]:
            kp = "".join(f"<li>{e(p)}</li>" for p in s.get("key_points", []))
            out.append(
                f'<article class="card" id="{s["sid"]}"><div><span class="tag">{e(t["title"])}</span>'
                f'<span class="tag">Önem {s["importance"]}/10</span></div><h3>{e(s["title"])}</h3>'
                f'{paras(s.get("detail")) or "<p>" + e(s["headline"]) + "</p>"}'
                f'{"<ul class=kp>" + kp + "</ul>" if kp else ""}{sources_html(s["sources"])}'
                f'<div class="muted" style="margin-top:.5rem"><a href="#">Başa dön</a></div></article>')
    out.append("<h2>Piyasalar</h2>" + (market_table(b.get("markets", [])) or '<p class="empty">Veri yok.</p>'))
    st = b.get("stats", {})
    out.append(f'<p class="muted">Taranan haber: {st.get("items", "?")} | Model: {e(st.get("model"))} | '
               f'Token: {st.get("prompt_tokens", 0):,} girdi, {st.get("completion_tokens", 0):,} çıktı'
               f'{" | Tahmini maliyet: $" + str(st["cost_usd"]) if "cost_usd" in st else ""}</p>')
    return page(f"Bülten {tr_date(d)}", "".join(out), 1)


def render_research(r: dict) -> str:
    out = [f'<nav class="top"><a href="../index.html">Ana sayfa</a><span class="muted">{e(r["created"])}</span></nav>',
           f'<h1>{e(r["title"])}</h1><div class="muted">Sorgu: {e(r["query"])}</div>']
    if r.get("summary_points"):
        out.append('<h2>Özet</h2><ul class="sum">' + "".join(f"<li>{e(p)}</li>" for p in r["summary_points"]) + "</ul>")
    for s in r.get("sections", []):
        out.append(f'<h2>{e(s.get("heading"))}</h2>{paras(s.get("body"))}')
    if r.get("caveats"):
        out.append(f'<p class="muted">Not: {e(r["caveats"])}</p>')
    out.append('<h2>Kaynaklar</h2>' + sources_html(r.get("sources", [])).replace("<details>", "<details open>"))
    return page(r["title"], "".join(out), 1)


def render_all() -> None:
    DOCS.mkdir(exist_ok=True)
    (DOCS / "style.css").write_text(CSS.strip(), encoding="utf-8")
    (DOCS / ".nojekyll").write_text("", encoding="utf-8")
    (DOCS / "manifest.json").write_text(json.dumps(
        {"name": "Haber Bülteni", "short_name": "Bülten", "start_url": "./index.html",
         "display": "standalone", "background_color": "#121212", "theme_color": "#1f5fbf"},
        ensure_ascii=False), encoding="utf-8")

    bulletins = []
    for p in sorted((DATA / "bulletins").glob("*.json"), reverse=True):
        b = json.loads(p.read_text(encoding="utf-8"))
        _write(DOCS / "b" / f"{b['date']}.html", render_bulletin(b))
        bulletins.append(b)
    research = []
    for p in sorted((DATA / "research").glob("*.json"), reverse=True):
        r = json.loads(p.read_text(encoding="utf-8"))
        _write(DOCS / "r" / f"{r['id']}.html", render_research(r))
        research.append(r)

    body = ['<h1>Haber Bülteni</h1>']
    if bulletins:
        latest = bulletins[0]
        body.append(f'<p><a href="b/{latest["date"]}.html"><b>Son bülten: {e(tr_date(latest["date"]))}</b></a></p>')
    body.append('<h2>Bültenler</h2><div class="list">' + "".join(
        f'<a href="b/{b["date"]}.html">{e(tr_date(b["date"]))} '
        f'<span class="muted">({sum(len(t["stories"]) for t in b["topics"])} haber)</span></a>'
        for b in bulletins[:60]) + "</div>")
    if research:
        body.append('<h2>Araştırmalar</h2><div class="list">' + "".join(
            f'<a href="r/{r["id"]}.html">{e(r["title"])} <span class="muted">{e(r["created"])}</span></a>'
            for r in research[:60]) + "</div>")
    _write(DOCS / "index.html", page("Haber Bülteni", "".join(body), 0))


def _write(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")
