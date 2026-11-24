"""Uygulamadaki "Konularım" ekranından gelen değişiklikler.

config.yaml elle düzenlenen ana dosyadır (yorumları korunur). Uygulamadan gelen değişiklikler ayrı bir
dosyada tutulur: data/user_topics.json
  {"levels": {"<id>": "az|orta|cok"}, "removed": ["<id>"], "added": [<konu sözlüğü>]}
load_config() bu dosyayı config.yaml'ın üstüne uygular.
"""
from __future__ import annotations

import re

from .common import DATA, read_json, write_json

USER_FILE = DATA / "user_topics.json"

# Seviye -> (en düşük önem puanı, en fazla haber). Uygulamadaki Az/Orta/Çok ile aynı.
LEVELS = {"az": (8, 3), "orta": (7, 5), "cok": (6, 8)}


def level_of(topic: dict) -> str:
    """Konunun etkin seviyesi (config'deki puan eşiğinden çıkarılır)."""
    mi = int(topic.get("min_importance", 7))
    return "az" if mi >= 8 else "orta" if mi == 7 else "cok"


def apply_user(cfg: dict) -> dict:
    user = read_json(USER_FILE, {}) or {}
    removed = set(user.get("removed", []))
    topics = [t for t in cfg.get("topics", []) if t["id"] not in removed]
    known = {t["id"] for t in topics}
    topics += [t for t in user.get("added", []) if t.get("id") and t["id"] not in known and t["id"] not in removed]
    for t in topics:
        lv = user.get("levels", {}).get(t["id"])
        if lv in LEVELS:
            t["min_importance"], t["max_stories"] = LEVELS[lv]
    cfg["topics"] = topics
    return cfg


def public_list(cfg_raw: dict) -> dict:
    """Uygulamaya gidecek konu listesi (api/topics.json): etkin konular ve geri eklenebilecek kaldırılanlar."""
    user = read_json(USER_FILE, {}) or {}
    added = {t["id"] for t in user.get("added", [])}
    removed = set(user.get("removed", []))
    active = apply_user(dict(cfg_raw, topics=[dict(t) for t in cfg_raw.get("topics", [])]))["topics"]
    return {
        "topics": [{"id": t["id"], "title": t["title"], "level": level_of(t), "custom": t["id"] in added}
                   for t in active],
        "removed": [{"id": t["id"], "title": t["title"], "level": level_of(t), "custom": False}
                    for t in cfg_raw.get("topics", []) if t["id"] in removed],
    }


GEN_SYSTEM = """A Turkish user wants a new topic in their daily personal news bulletin.
From their short description, produce the topic definition.
- title: short Turkish title (max 6 words).
- criteria: Turkish, 4-8 lines. Who the user is regarding this topic (only what the description implies),
  what counts as important (concrete event types) and what is unimportant (noise, clickbait, minor rumors).
- queries: news search queries. "en": 1-3 (always), "tr": 1-2 if Turkish news likely, "zh": 0-2 only for
  tech/hardware/AI/China topics, "es": 0-1 only for Spanish football/Spain. Short keyword queries;
  OR operators allowed like: GPU OR Radeon.
Output JSON: {"title": str, "criteria": str, "queries": {"en": [], "tr": [], "zh": [], "es": []}}"""


def _slug(s: str) -> str:
    import unicodedata
    s = unicodedata.normalize("NFKD", s.replace("ı", "i")).encode("ascii", "ignore").decode()
    return re.sub(r"[^a-z0-9]+", "_", s.lower()).strip("_")[:30] or "konu"


def change(cfg_raw: dict, action: str, topic_id: str = "", text: str = "", level: str = "") -> str:
    """Bir değişikliği uygular, sonucu açıklayan kısa Türkçe metin döner. cfg_raw: user dosyası uygulanmamış config."""
    user = read_json(USER_FILE, {}) or {}
    user.setdefault("levels", {})
    user.setdefault("removed", [])
    user.setdefault("added", [])
    base_ids = {t["id"] for t in cfg_raw.get("topics", [])}
    added_ids = {t["id"] for t in user["added"]}

    if action == "level":
        if level not in LEVELS:
            raise ValueError(f"geçersiz seviye: {level}")
        if topic_id not in base_ids | added_ids:
            raise ValueError(f"bilinmeyen konu: {topic_id}")
        user["levels"][topic_id] = level
        msg = f"{topic_id} seviyesi {level}"
    elif action == "remove":
        if topic_id in added_ids:
            user["added"] = [t for t in user["added"] if t["id"] != topic_id]
        elif topic_id in base_ids:
            if topic_id not in user["removed"]:
                user["removed"].append(topic_id)
        else:
            raise ValueError(f"bilinmeyen konu: {topic_id}")
        user["levels"].pop(topic_id, None)
        msg = f"{topic_id} kaldırıldı"
    elif action == "restore":
        user["removed"] = [i for i in user["removed"] if i != topic_id]
        msg = f"{topic_id} geri eklendi"
    elif action == "add":
        text = text.strip()
        if not 2 <= len(text) <= 300:
            raise ValueError("konu açıklaması 2-300 karakter olmalı")
        lv = level if level in LEVELS else "orta"
        from .llm import DeepSeek
        res = DeepSeek(cfg_raw.get("model", "deepseek-flash")).json(GEN_SYSTEM, f"User's description: {text}",
                                                                   max_tokens=3000)
        title = (res.get("title") or text)[:60]
        tid = _slug(title)
        while tid in base_ids | added_ids:
            tid += "_2"
        google = []
        for lang in ("tr", "en", "zh", "es"):
            for q in (res.get("queries") or {}).get(lang, [])[:3]:
                if isinstance(q, str) and q.strip():
                    google.append({"q": q.strip()[:120], "lang": lang})
        if not google:
            google = [{"q": text[:120], "lang": "tr"}]
        mi, ms = LEVELS[lv]
        user["added"].append({
            "id": tid, "title": title, "min_importance": mi, "max_stories": ms,
            "criteria": (res.get("criteria") or text).strip() + "\n",
            "sources": {"google_news": google},
            "user_request": text,
        })
        msg = f"yeni konu eklendi: {title} ({tid})"
    else:
        raise ValueError(f"bilinmeyen işlem: {action}")

    write_json(USER_FILE, user)
    return msg
