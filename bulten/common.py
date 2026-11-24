from __future__ import annotations

import json
from datetime import datetime, timezone, timedelta
from pathlib import Path

import yaml

ROOT = Path(__file__).resolve().parent.parent
DATA = ROOT / "data"
DOCS = ROOT / "docs"
PENDING = ROOT / ".pending"
TR_TZ = timezone(timedelta(hours=3))  # Türkiye'de yaz saati yok, sabit UTC+3

AYLAR = ["Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz",
         "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"]


def load_config(raw: bool = False) -> dict:
    """config.yaml; raw=False ise uygulamadan gelen konu değişiklikleri (data/user_topics.json) uygulanmış hali."""
    with open(ROOT / "config.yaml", encoding="utf-8") as f:
        cfg = yaml.safe_load(f)
    if raw:
        return cfg
    from .topics import apply_user
    return apply_user(cfg)


def now_tr() -> datetime:
    return datetime.now(TR_TZ)


def tr_date(d: str) -> str:
    """'2026-11-24' -> '24 Kasım 2026'"""
    y, m, day = d.split("-")
    return f"{int(day)} {AYLAR[int(m) - 1]} {y}"


def pct_text(p: float) -> str:
    """-25.7 -> '%25,7 düşüş'"""
    return f"%{abs(p):.1f}".replace(".", ",") + (" artış" if p > 0 else " düşüş")


def utc_to_tr(iso: str) -> str:
    """'2026-11-23T22:46Z' -> '24.11.2026 01:46'"""
    try:
        d = datetime.strptime(iso, "%Y-%m-%dT%H:%MZ").replace(tzinfo=timezone.utc).astimezone(TR_TZ)
        return d.strftime("%d.%m.%Y %H:%M")
    except ValueError:
        return iso


def read_json(path: Path, default=None):
    if not path.exists():
        return default
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def write_json(path: Path, obj) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, ensure_ascii=False, indent=1)


def queue_notification(messages: list[str]) -> None:
    """Mesajları, git push'tan sonra gönderilmek üzere sıraya koyar."""
    path = PENDING / "notify.json"
    q = read_json(path, [])
    q.extend(messages)
    write_json(path, q)
