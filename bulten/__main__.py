"""Komut satırı:
  python -m bulten daily [--date YYYY-AA-GG]   günlük bülteni hazırla (geçmiş gün de olabilir)
  python -m bulten research "konu"
  python -m bulten render               docs/ sitesini yeniden üret
  python -m bulten staged prep|next     API'siz adımlı bülten (seçimi dışarıdaki ajan yapar)
  python -m bulten topics <add|remove|restore|level|save> uygulamadan gelen konu değişikliği
                                        (ortam: TOPIC_ID, TOPIC_TEXT, TOPIC_LEVEL)
"""
from __future__ import annotations

import sys

from .common import load_config


def cmd_daily(date: str | None = None) -> None:
    from .daily import run_daily
    run_daily(load_config(), date)


def cmd_research(query: str) -> None:
    from .research import run_research
    run_research(load_config(), query)


def main(argv: list[str]) -> None:
    if not argv:
        print(__doc__)
        return
    args = argv
    c = args[0]
    if c == "daily":
        date = args[args.index("--date") + 1] if "--date" in args and args.index("--date") + 1 < len(args) else None
        if date:
            import re
            if not re.fullmatch(r"\d{4}-\d{2}-\d{2}", date):
                raise SystemExit(f"Geçersiz tarih: {date}")
        cmd_daily(date)
    elif c == "research":
        cmd_research(" ".join(args[1:]))
    elif c == "render":
        from .render import render_all
        render_all()
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
