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

data class MarketAlarm(val symbol: String, val name: String, val lines: List<String>)

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
 * Haber miktarı seviyesi. Sunucu her başlık için puanlı haberler gönderir, filtreleme burada yapılır.
 * Böylece ileride herkese ortak üretilen bültenden her kullanıcı kendi seviyesine göre görebilir.
 */
enum class Level(val label: String, val minImportance: Int, val maxStories: Int) {
    AZ("Az", 8, 3),
    ORTA("Orta", 7, 6),
    COK("Çok", 6, 10);

    fun apply(stories: List<Story>): List<Story> =
        stories.filter { it.importance >= minImportance }.sortedByDescending { it.importance }.take(maxStories)
}

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
