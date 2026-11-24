package com.marjinalbaykus.bulten.data

import org.json.JSONArray
import org.json.JSONObject

data class Source(val title: String, val url: String, val source: String, val lang: String)

data class Story(
    val sid: String,
    val importance: Int,
    val headline: String,
    val title: String,
    val detail: String,
    val keyPoints: List<String>,
    val sources: List<Source>,
)

data class Topic(val id: String, val title: String, val note: String, val stories: List<Story>)

/** Eşiği aşan hareketler; points: ("1 günde", 4.7) gibi pencere ve yüzde. */
data class MarketAlarm(val symbol: String, val name: String, val lines: List<String>, val points: List<Pair<String, Double>>)

/** Bir piyasa varlığının son değeri ve 1 günlük yüzde değişimi. */
data class Market(val symbol: String, val name: String, val value: Double, val dayPct: Double?)

data class Bulletin(
    val date: String,
    val generatedAt: String,
    val topics: List<Topic>,
    val alarms: List<MarketAlarm>,
    val markets: List<Market>,
)

data class BulletinRef(val date: String, val generatedAt: String, val count: Int)

data class ResearchRef(val id: String, val title: String, val query: String, val created: String)

data class Index(val bulletins: List<BulletinRef>, val research: List<ResearchRef>)

/**
 * Konu başına haber miktarı. Sunucu bülteni bu ayara göre hazırlar (data/user_topics.json, bulten/topics.py).
 * key: sunucudaki ad; min/max: en düşük önem puanı ve en fazla haber.
 */
enum class Level(val key: String, val label: String, val minImportance: Int, val maxStories: Int) {
    AZ("az", "Az", 8, 3),
    ORTA("orta", "Orta", 7, 5),
    COK("cok", "Çok", 6, 8);

    companion object {
        fun of(key: String): Level = entries.firstOrNull { it.key == key } ?: ORTA
    }
}

data class TopicInfo(val id: String, val title: String, val level: Level, val custom: Boolean)

data class Topics(val active: List<TopicInfo>, val removed: List<TopicInfo>)

private fun JSONArray?.objects(): List<JSONObject> =
    if (this == null) emptyList() else (0 until length()).mapNotNull { optJSONObject(it) }

private fun JSONArray?.strings(): List<String> =
    if (this == null) emptyList() else (0 until length()).mapNotNull { optString(it).takeIf { s -> s.isNotBlank() } }

fun parseBulletin(text: String): Bulletin {
    val o = JSONObject(text)
    val topics = o.optJSONArray("topics").objects().map { t ->
        Topic(
            id = t.optString("id"),
            title = t.optString("title"),
            note = t.optString("note"),
            stories = t.optJSONArray("stories").objects().map { s ->
                Story(
                    sid = s.optString("sid"),
                    importance = s.optInt("importance"),
                    headline = s.optString("headline"),
                    title = s.optString("title").ifBlank { s.optString("headline") },
                    detail = s.optString("detail"),
                    keyPoints = s.optJSONArray("key_points").strings(),
                    sources = s.optJSONArray("sources").objects().map { src ->
                        Source(src.optString("title"), src.optString("url"), src.optString("source"), src.optString("lang"))
                    },
                )
            }.sortedByDescending { it.importance },
        )
    }
    val alarms = o.optJSONArray("market_alarms").objects().map { m ->
        MarketAlarm(
            symbol = m.optString("symbol"),
            name = m.optString("name"),
            lines = m.optJSONArray("alarms").objects().map { a ->
                val pct = a.optDouble("pct")
                "${a.optString("label")} " + (if (pct > 0) "+" else "") + String.format(java.util.Locale.US, "%.1f", pct) + "%"
            },
            points = m.optJSONArray("alarms").objects().map { a -> a.optString("label") to a.optDouble("pct") },
        )
    }
    val markets = o.optJSONArray("markets").objects().map { m ->
        val day = m.optJSONObject("changes")?.takeIf { it.has("day") }?.optDouble("day")
        Market(m.optString("symbol"), m.optString("name"), m.optDouble("value"), day)
    }
    return Bulletin(o.optString("date"), o.optString("generated_at"), topics, alarms, markets)
}

fun parseIndex(text: String): Index {
    val o = JSONObject(text)
    return Index(
        bulletins = o.optJSONArray("bulletins").objects().map {
            BulletinRef(it.optString("date"), it.optString("generated_at"), it.optInt("count"))
        },
        research = o.optJSONArray("research").objects().map {
            ResearchRef(it.optString("id"), it.optString("title"), it.optString("query"), it.optString("created"))
        },
    )
}

data class ResearchSection(val heading: String, val body: String)

data class Research(
    val id: String,
    val query: String,
    val title: String,
    val created: String,
    val summaryPoints: List<String>,
    val sections: List<ResearchSection>,
    val caveats: String,
    val sources: List<Source>,
)

fun parseResearch(text: String): Research {
    val o = JSONObject(text)
    return Research(
        id = o.optString("id"),
        query = o.optString("query"),
        title = o.optString("title"),
        created = o.optString("created"),
        summaryPoints = o.optJSONArray("summary_points").strings(),
        sections = o.optJSONArray("sections").objects().map { ResearchSection(it.optString("heading"), it.optString("body")) },
        caveats = o.optString("caveats"),
        sources = o.optJSONArray("sources").objects().map { src ->
            Source(src.optString("title"), src.optString("url"), src.optString("source"), src.optString("lang"))
        },
    )
}

fun parseTopics(text: String): Topics {
    val o = JSONObject(text)
    fun list(name: String) = o.optJSONArray(name).objects().map {
        TopicInfo(it.optString("id"), it.optString("title"), Level.of(it.optString("level")), it.optBoolean("custom"))
    }
    return Topics(list("topics"), list("removed"))
}
