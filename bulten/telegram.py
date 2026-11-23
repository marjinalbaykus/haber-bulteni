"""Telegram bot: mesaj gönderme ve gelen komutları okuma (getUpdates ile, webhook yok)."""
from __future__ import annotations

import html
import os

import requests

from .common import tr_date, pct_text

LIMIT = 3900  # Telegram sınırı 4096


def _api(method: str, **params):
    token = os.environ["TELEGRAM_BOT_TOKEN"]
    r = requests.post(f"https://api.telegram.org/bot{token}/{method}", json=params, timeout=60)
    data = r.json()
    if not data.get("ok"):
        raise RuntimeError(f"Telegram {method} hatası: {data.get('description')}")
    return data["result"]


def chat_id() -> str:
    return os.environ["TELEGRAM_CHAT_ID"]


def send(text: str) -> None:
    _api("sendMessage", chat_id=chat_id(), text=text, parse_mode="HTML",
         link_preview_options={"is_disabled": True})


def send_many(messages: list[str]) -> None:
    for m in messages:
        send(m)


def get_updates(offset: int | None = None) -> list[dict]:
    params = {"timeout": 0, "allowed_updates": ["message"]}
    if offset is not None:
        params["offset"] = offset
    return _api("getUpdates", **params)


def _pack(blocks: list[str]) -> list[str]:
    """Blokları Telegram sınırını aşmayacak şekilde mesajlara böler."""
    msgs, cur = [], ""
    for b in blocks:
        if cur and len(cur) + len(b) + 2 > LIMIT:
            msgs.append(cur)
            cur = ""
        cur = f"{cur}\n\n{b}" if cur else b
    if cur:
        msgs.append(cur)
    return msgs


def e(s: str) -> str:
    return html.escape(s or "", quote=False)


def bulletin_messages(b: dict, site_url: str) -> list[str]:
    page = f"{site_url}/b/{b['date']}.html"
    blocks = [f"<b>Günün Özeti - {e(tr_date(b['date']))}</b>"]
    if b.get("market_alarms"):
        lines = ["<b>Piyasa alarmı</b>"]
        for m in b["market_alarms"]:
            moves = ", ".join(f"{a['label']} {pct_text(a['pct'])}" for a in m["alarms"])
            lines.append(f"• {e(m['name'])}: {moves}")
        blocks.append("\n".join(lines))
    for t in b["topics"]:
        lines = [f"<b>{e(t['title'])}</b>"]
        if t["stories"]:
            for s in t["stories"]:
                lines.append(f'• <a href="{page}#{s["sid"]}">{e(s["headline"])}</a>')
        else:
            lines.append(f"<i>{e(t['note'])}</i>")
        blocks.append("\n".join(lines))
    blocks.append(f'<a href="{page}">Bültenin tamamı ve kaynaklar</a>')
    return _pack(blocks)


def research_messages(r: dict, site_url: str) -> list[str]:
    page = f"{site_url}/r/{r['id']}.html"
    blocks = [f"<b>Araştırma: {e(r['title'])}</b>", f"<i>Sorgu: {e(r['query'])}</i>"]
    pts = "\n".join(f"• {e(p)}" for p in r.get("summary_points", []))
    if pts:
        blocks.append(pts)
    blocks.append(f'<a href="{page}">Detaylı rapor ve kaynaklar</a>')
    return _pack(blocks)
