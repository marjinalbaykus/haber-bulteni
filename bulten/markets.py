"""Piyasa verisi: sadece eşiği aşan büyük hareketleri alarm olarak işaretler."""
from __future__ import annotations

from datetime import timedelta, datetime, timezone

from .sources import session

WINDOWS = {"day": ("1 günde", None), "week": ("7 günde", 7), "month": ("30 günde", 30)}
OUNCE_GRAMS = 31.1035


def _series(symbol: str):
    r = session().get(f"https://query1.finance.yahoo.com/v8/finance/chart/{symbol}",
                      params={"range": "3mo", "interval": "1d"}, timeout=20)
    r.raise_for_status()
    res = r.json()["chart"]["result"][0]
    ts = res.get("timestamp") or []
    closes = res["indicators"]["quote"][0]["close"]
    pts = [(datetime.fromtimestamp(t, tz=timezone.utc), c) for t, c in zip(ts, closes) if c is not None]
    if len(pts) < 2:
        raise ValueError("yetersiz veri")
    return pts


def _changes(pts) -> dict:
    last_t, last = pts[-1]
    out = {"day": (last / pts[-2][1] - 1) * 100}
    for key, (_, days) in WINDOWS.items():
        if days is None:
            continue
        target = last_t - timedelta(days=days)
        older = [c for t, c in pts if t <= target]
        if older:
            out[key] = (last / older[-1] - 1) * 100
    return {"value": last, "changes": out}


def fetch_markets(cfg_markets: list[dict]) -> list[dict]:
    results: dict[str, dict] = {}
    errors = []
    for m in cfg_markets:
        if m["symbol"] == "GRAM":
            continue
        try:
            results[m["symbol"]] = _changes(_series(m["symbol"]))
        except Exception as e:
            errors.append(f"{m['symbol']}: {e}")
    # Gram altın (TL) = ons altın * USDTRY / 31.1035; değişimler bileşik hesaplanır.
    if "GC=F" in results and "USDTRY=X" in results:
        g, u = results["GC=F"], results["USDTRY=X"]
        ch = {k: ((1 + g["changes"][k] / 100) * (1 + u["changes"][k] / 100) - 1) * 100
              for k in g["changes"] if k in u["changes"]}
        results["GRAM"] = {"value": g["value"] * u["value"] / OUNCE_GRAMS, "changes": ch}

    out = []
    for m in cfg_markets:
        r = results.get(m["symbol"])
        if not r:
            continue
        alarms = []
        for key, (label, _) in WINDOWS.items():
            v = r["changes"].get(key)
            if v is not None and m.get(key) is not None and abs(v) >= m[key]:
                alarms.append({"window": key, "label": label, "pct": round(v, 1)})
        out.append({"symbol": m["symbol"], "name": m["name"], "group": m.get("group", ""),
                    "value": round(r["value"], 2),
                    "changes": {k: round(v, 2) for k, v in r["changes"].items()},
                    "alarms": alarms})
    if errors:
        print("Piyasa verisi hataları:", "; ".join(errors))
    return out


def market_lines(markets: list[dict], group: str | None = None) -> str:
    """LLM'e verilecek kısa piyasa özeti."""
    lines = []
    for m in markets:
        if group and m["group"] != group:
            continue
        ch = ", ".join(f"{WINDOWS[k][0]} %{v:+.1f}" for k, v in m["changes"].items())
        flag = " [EŞİK AŞILDI]" if m["alarms"] else ""
        lines.append(f"- {m['name']}: {m['value']} ({ch}){flag}")
    return "\n".join(lines)
