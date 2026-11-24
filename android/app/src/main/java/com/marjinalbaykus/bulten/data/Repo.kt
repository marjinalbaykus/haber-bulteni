package com.marjinalbaykus.bulten.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** Sonuç ve verinin internetten mi yoksa telefondaki kopyadan mı geldiği. */
data class Loaded<T>(val value: T, val offline: Boolean)

class Repo(context: Context) {
    private val cache = File(context.filesDir, "api").apply { mkdirs() }
    private val prefs = context.getSharedPreferences("ayarlar", Context.MODE_PRIVATE)

    suspend fun index(): Loaded<Index> = load("index.json", ::parseIndex)

    suspend fun bulletin(date: String): Loaded<Bulletin> = load("b/$date.json", ::parseBulletin)

    /** Kullanıcının başlık için seçtiği seviye; null ise sunucunun gönderdiği hepsi gösterilir. */
    fun level(topicId: String): Level? =
        prefs.getString("seviye_$topicId", null)?.let { name -> Level.entries.firstOrNull { it.name == name } }

    fun setLevel(topicId: String, level: Level?) {
        prefs.edit().apply { if (level == null) remove("seviye_$topicId") else putString("seviye_$topicId", level.name) }.apply()
    }

    fun visible(topic: Topic): List<Story> = level(topic.id)?.apply(topic.stories) ?: topic.stories

    private suspend fun <T> load(path: String, parse: (String) -> T): Loaded<T> = withContext(Dispatchers.IO) {
        val file = File(cache, path.replace('/', '_'))
        try {
            val text = fetch(path)
            val value = parse(text)  // önce çözümle, bozuk veriyle önbelleği ezme
            file.writeText(text)
            Loaded(value, offline = false)
        } catch (e: Exception) {
            if (file.exists()) Loaded(parse(file.readText()), offline = true) else throw e
        }
    }

    private fun fetch(path: String): String {
        // GitHub Pages yanıtları birkaç dakika önbellekte tutar; dakikalık parametre bunu aşar.
        val url = URL("$BASE$path?t=${System.currentTimeMillis() / 60_000}")
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 15_000
        conn.readTimeout = 20_000
        conn.useCaches = false
        try {
            if (conn.responseCode != 200) error("Sunucu yanıtı: ${conn.responseCode}")
            return conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }

    companion object {
        const val BASE = "https://marjinalbaykus.github.io/haber-bulteni/api/"
    }
}
