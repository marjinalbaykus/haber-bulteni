"""Telegram bot: mesaj gönderme ve gelen komutları okuma (getUpdates ile, webhook yok)."""
from __future__ import annotations

import html
import os
import time
from datetime import datetime, timezone

import requests

from .common import tr_date, pct_text, TR_TZ

LIMIT = 3900  # Telegram sınırı 4096


def _api(method: str, retries: int = 6, **params):
    token = os.environ["TELEGRAM_BOT_TOKEN"]
    data = {}
    for _ in range(retries):
        r = requests.post(f"https://api.telegram.org/bot{token}/{method}", json=params, timeout=60)
        data = r.json()
        if data.get("ok"):
            return data["result"]
        wait = (data.get("parameters") or {}).get("retry_after")
        if data.get("error_code") != 429 or not wait:
            break
        print(f"Telegram {method}: {wait} sn bekleniyor (istek sınırı)")
        time.sleep(wait + 1)
    raise RuntimeError(f"Telegram {method} hatası: {data.get('description')}")


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


def update_messages(b: dict, site_url: str) -> list[str]:
    """Aynı günün bülteni güncellendiyse (birleştirme): sadece bu çalıştırmayla eklenen haberler.
    Yeni haber yoksa boş liste (hiçbir şey gönderilmez)."""
    page = f"{site_url}/b/{b['date']}.html"
    at = b["updated_at"]
    topics = [(t, [s for s in t["stories"] if s.get("added_at") == at]) for t in b["topics"]]
    topics = [(t, st) for t, st in topics if st]
    n = sum(len(st) for _, st in topics)
    if not n:
        return []
    try:
        hhmm = datetime.strptime(at, "%Y-%m-%dT%H:%MZ").replace(tzinfo=timezone.utc).astimezone(TR_TZ).strftime("%H:%M")
    except ValueError:
        hhmm = at
    blocks = [f"<b>Güncelleme ({hhmm}): {n} yeni haber</b>"]
    for t, st in topics:
        lines = [f"<b>{e(t['title'])}</b>"]
        lines += [f'• <a href="{page}#{s["sid"]}">{e(s["headline"])}</a>' for s in st]
        blocks.append("\n".join(lines))
    blocks.append(f'<a href="{page}">Bültenin tamamı ve kaynaklar</a>')
    return _pack(blocks)


def bulletin_messages(b: dict, site_url: str) -> list[str]:
    if b.get("updated_at"):
        return update_messages(b, site_url)
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
