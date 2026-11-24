"""Komut satırı:
  python -m bulten daily [--send]       günlük bülteni hazırla
  python -m bulten research "konu" [--send]
  python -m bulten poll                 Telegram'dan gelen komutları işle
  python -m bulten render               docs/ sitesini yeniden üret
  python -m bulten notify               sıradaki Telegram mesajlarını gönder
  python -m bulten chatid               bota yazan sohbetlerin id'lerini göster
  python -m bulten staged prep|next     API'siz adımlı bülten (seçimi dışarıdaki ajan yapar)
  python -m bulten topics <add|remove|restore|level|save> uygulamadan gelen konu değişikliği
                                        (ortam: TOPIC_ID, TOPIC_TEXT, TOPIC_LEVEL)
"""
from __future__ import annotations

import sys
import traceback

from . import telegram as tg
from .common import PENDING, load_config, read_json, queue_notification

HELP = ("<b>Komutlar</b>\n"
        "• <code>tara</code> : günlük bülteni şimdi hazırlar\n"
        "• <code>araştır konu</code> : konuyu araştırıp rapor hazırlar\n"
        "  (<code>tara konu</code> da aynı işi yapar)\n"
        "Cevap birkaç dakika içinde gelir.")

def _norm(word: str) -> str:
    """'/Araştır@bot' -> 'arastir' (Türkçe büyük I/İ dahil)."""
    w = word.lstrip("/").split("@")[0].replace("I", "ı").replace("İ", "i").lower()
    return w.translate(str.maketrans("ışğüöç", "isguoc"))


def parse_command(text: str) -> tuple[str, str]:
    text = (text or "").strip()
    if not text:
        return "", ""
    head, _, rest = text.partition(" ")
    cmd, rest = _norm(head), rest.strip()
    if cmd in ("arastir", "ara", "research"):
        return ("research", rest) if rest else ("help", "")
    if cmd in ("tara", "bulten", "ozet"):
        return ("research", rest) if rest else ("daily", "")
    if cmd in ("yardim", "help", "start", "komutlar"):
        return "help", ""
    return "unknown", text


def deliver(messages: list[str], send_now: bool) -> None:
    if send_now:
        tg.send_many(messages)
    else:
        queue_notification(messages)


def cmd_daily(send_now: bool) -> None:
    from .daily import run_daily
    cfg = load_config()
    b = run_daily(cfg)
    deliver(tg.bulletin_messages(b, cfg["site_url"]), send_now)


def cmd_research(query: str, send_now: bool) -> None:
    from .research import run_research
    cfg = load_config()
    r = run_research(cfg, query)
    deliver(tg.research_messages(r, cfg["site_url"]), send_now)


def cmd_poll() -> None:
    updates = tg.get_updates()
    if not updates:
        print("Yeni mesaj yok.")
        return
    tg.get_updates(offset=max(u["update_id"] for u in updates) + 1)  # alındı olarak işaretle
    allowed = str(tg.chat_id())
    for u in updates:
        msg = u.get("message") or {}
        if str((msg.get("chat") or {}).get("id")) != allowed:
            print("Yetkisiz sohbetten mesaj yok sayıldı.")
            continue
        cmd, arg = parse_command(msg.get("text", ""))
        print("Komut:", cmd, arg)
        try:
            if cmd == "research":
                tg.send(f"Araştırıyorum: <b>{tg.e(arg)}</b>\nBirkaç dakika sürebilir.")
                cmd_research(arg, send_now=False)
            elif cmd == "daily":
                tg.send("Bülten hazırlanıyor, birkaç dakika sürebilir.")
                cmd_daily(send_now=False)
            elif cmd == "help":
                tg.send(HELP)
            elif cmd == "unknown":
                tg.send("Anlayamadım.\n\n" + HELP)
        except Exception as ex:
            traceback.print_exc()
            tg.send(f"Hata oluştu: {tg.e(str(ex)[:300])}")


def cmd_notify() -> None:
    path = PENDING / "notify.json"
    msgs = read_json(path, [])
    if msgs:
        tg.send_many(msgs)
        print(f"{len(msgs)} mesaj gönderildi.")
    path.unlink(missing_ok=True)


def cmd_resend() -> None:
    """Son bülteni yeniden üretmeden Telegram'a tekrar gönderir."""
    from .common import DATA
    cfg = load_config()
    files = sorted((DATA / "bulletins").glob("*.json"))
    if not files:
        print("Gönderilecek bülten yok.")
        return
    b = read_json(files[-1])
    tg.send_many(tg.bulletin_messages(b, cfg["site_url"]))
    print("Gönderildi:", b["date"])


def cmd_chatid() -> None:
    for u in tg.get_updates():
        chat = (u.get("message") or {}).get("chat") or {}
        print(chat.get("id"), chat.get("first_name") or chat.get("title"))


def main(argv: list[str]) -> None:
    if not argv:
        print(__doc__)
        return
    send_now = "--send" in argv
    args = [a for a in argv if a != "--send"]
    c = args[0]
    if c == "daily":
        cmd_daily(send_now)
    elif c == "research":
        cmd_research(" ".join(args[1:]), send_now)
    elif c == "poll":
        cmd_poll()
    elif c == "render":
        from .render import render_all
        render_all()
    elif c == "notify":
        cmd_notify()
    elif c == "chatid":
        cmd_chatid()
    elif c == "resend":
        cmd_resend()
    elif c == "topics":
        import os
        from .topics import change
        print(change(load_config(raw=True), args[1] if len(args) > 1 else "",
                     os.environ.get("TOPIC_ID", ""), os.environ.get("TOPIC_TEXT", ""),
                     os.environ.get("TOPIC_LEVEL", "")))
    elif c == "staged":
        from .staged import main as staged_main
        staged_main(args[1:])
    else:
        print(__doc__)
        sys.exit(1)


if __name__ == "__main__":
    main(sys.argv[1:])
