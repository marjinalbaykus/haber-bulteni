"""DeepSeek API istemcisi (OpenAI uyumlu /chat/completions, JSON modu)."""
from __future__ import annotations

import json
import os
import threading
import time
from datetime import datetime, timezone

import requests

API_URL = "https://api.deepseek.com/chat/completions"
# Kullanıcı kararı: sadece Flash kullanılır. Pro (ve başka model) config'e yazılsa bile reddedilir.
ALLOWED_MODELS = {"deepseek-flash"}


# deepseek-flash fiyatları, $ / 1M token (api-docs.deepseek.com/quick_start/pricing, 2026-11-24).
# Yoğun saatler: hafta içi 01-04 ve 06-10 UTC; diğer saatlerde fiyat yarıya iner.
PRICE_PEAK = {"hit": 0.006, "miss": 0.3, "out": 1.2}


def is_peak(utc: datetime) -> bool:
    return utc.weekday() < 5 and (1 <= utc.hour < 4 or 6 <= utc.hour < 10)


def estimate_cost(usage: dict, utc: datetime | None = None) -> float:
    utc = utc or datetime.now(timezone.utc)
    k = 1.0 if is_peak(utc) else 0.5
    hit = usage.get("cache_hit_tokens", 0)
    miss = usage.get("prompt_tokens", 0) - hit
    cost = (hit * PRICE_PEAK["hit"] + miss * PRICE_PEAK["miss"] + usage.get("completion_tokens", 0) * PRICE_PEAK["out"]) / 1e6
    return round(cost * k, 4)


class DeepSeek:
    def __init__(self, model: str = "deepseek-flash", api_key: str | None = None):
        if model not in ALLOWED_MODELS:
            raise RuntimeError(f"İzin verilmeyen model: {model}. Sadece deepseek-flash kullanılabilir.")
        self.model = model
        self.api_key = api_key or os.environ.get("DEEPSEEK_API_KEY")
        if not self.api_key:
            raise RuntimeError("DEEPSEEK_API_KEY tanımlı değil")
        self.usage = {"calls": 0, "prompt_tokens": 0, "completion_tokens": 0, "cache_hit_tokens": 0}
        self._lock = threading.Lock()

    def json(self, system: str, user: str, max_tokens: int = 16000, retries: int = 3,
             effort: str | None = None) -> dict:
        """effort: None = model varsayılanı; "none" = düşünme kapalı (çeviri/özet için yeterli, daha ucuz)."""
        body = {
            "model": self.model,
            "messages": [{"role": "system", "content": system}, {"role": "user", "content": user}],
            "response_format": {"type": "json_object"},
            "max_tokens": max_tokens,
        }
        if effort:
            body["reasoning_effort"] = effort
        last_err = None
        for attempt in range(retries):
            try:
                r = requests.post(API_URL, json=body, timeout=600,
                                  headers={"Authorization": f"Bearer {self.api_key}"})
                if r.status_code in (429, 500, 502, 503, 504):
                    raise RuntimeError(f"HTTP {r.status_code}: {r.text[:200]}")
                r.raise_for_status()
                data = r.json()
                u = data.get("usage") or {}
                with self._lock:
                    self.usage["calls"] += 1
                    self.usage["prompt_tokens"] += u.get("prompt_tokens", 0)
                    self.usage["completion_tokens"] += u.get("completion_tokens", 0)
                    self.usage["cache_hit_tokens"] += u.get("prompt_cache_hit_tokens", 0)
                content = data["choices"][0]["message"].get("content") or ""
                return json.loads(content)
            except Exception as e:
                last_err = e
                time.sleep(3 * (attempt + 1))
        raise RuntimeError(f"DeepSeek çağrısı başarısız: {last_err}")
