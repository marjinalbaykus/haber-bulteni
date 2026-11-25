package com.marjinalbaykus.bulten.data

import android.content.Context
import com.marjinalbaykus.bulten.ui.theme.ThemeMode
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

    suspend fun research(id: String): Loaded<Research> = load("r/$id.json", ::parseResearch)

    /** Konu listesi; gruplar telefonda seçilen sıraya dizilir. fresh: site önbelleğini her istekte aşar. */
    suspend fun topics(fresh: Boolean = false): Loaded<Topics> =
        load("topics.json", { parseTopics(it).withOrder(groupOrder) }, fresh)

    /** Grup sırası (grup kimlikleri). Hem ana sayfa hem Konular bu sırayı kullanır; sunucuya da gönderilir. */
    var groupOrder: List<String>
        get() = prefs.getString("grup_sirasi", "").orEmpty().split(',').filter { it.isNotBlank() }
        set(value) = prefs.edit().putString("grup_sirasi", value.joinToString(",")).apply()

    /** Sadece grup sırasını/üyeliğini sunucuya kaydeder (ana sayfadaki sıralama). */
    suspend fun saveGroups(groups: List<TopicGroup>) {
        val arr = org.json.JSONArray(groups.map { g ->
            org.json.JSONObject().put("id", g.id).put("name", g.name).put("topics", org.json.JSONArray(g.topics))
        })
        saveTopics(org.json.JSONObject().put("groups", arr).toString())
    }

    /** Ana sayfa: puana göre sırala (false = gruplara göre) ve sadece okunmamışları göster. */
    var sortByScore: Boolean
        get() = prefs.getBoolean("puana_gore", false)
        set(value) = prefs.edit().putBoolean("puana_gore", value).apply()

    var unreadOnly: Boolean
        get() = prefs.getBoolean("okunmamislar", false)
        set(value) = prefs.edit().putBoolean("okunmamislar", value).apply()

    /** Akış'ta kapatılmış (katlanmış) gruplar, bölüm anahtarıyla ("g-<grup kimliği>"). */
    var collapsedGroups: Set<String>
        get() = prefs.getStringSet("kapali_gruplar", emptySet())!!.toSet()
        set(value) = prefs.edit().putStringSet("kapali_gruplar", value).apply()

    /** Okunan haberler: "tarih/sid" (sid her bültende baştan numaralandığı için tarih de gerekir). */
    @Volatile private var readCache: Set<String>? = null

    fun readKeys(): Set<String> =
        readCache ?: prefs.getStringSet("okunan", emptySet())!!.toSet().also { readCache = it }

    fun markRead(key: String) {
        val cur = readKeys()
        if (key in cur) return
        val oldest = java.time.LocalDate.now(java.time.ZoneId.of("Europe/Istanbul")).minusDays(90).toString()
        val next = cur.filter { it.substringBefore('/') >= oldest }.toSet() + key   // 90 günden eskiler unutulur
        readCache = next
        prefs.edit().putStringSet("okunan", next).apply()
    }

    /**
     * Bir iş akışının [since] (ms) sonrası başlatılmış en son elle çalıştırmasının durumu; henüz görünmüyorsa null.
     * Konular kaydedilince sitenin yayınlanmasını beklemeden işin bittiği buradan anlaşılır.
     */
    suspend fun lastRun(workflow: String, since: Long): RunStatus? = withContext(Dispatchers.IO) {
        val token = githubToken
        if (token.isBlank()) return@withContext null
        val conn = URL("https://api.github.com/repos/$REPO/actions/workflows/$workflow/runs?event=workflow_dispatch&per_page=5")
            .openConnection() as HttpURLConnection
        try {
            conn.connectTimeout = 10_000
            conn.readTimeout = 15_000
            conn.useCaches = false
            conn.setRequestProperty("Accept", "application/vnd.github+json")
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
            if (conn.responseCode != 200) return@withContext null
            val runs = org.json.JSONObject(conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() })
                .optJSONArray("workflow_runs") ?: return@withContext null
            // Telefon ve GitHub saati arasındaki küçük farka pay bırakılır
            (0 until runs.length()).mapNotNull { runs.optJSONObject(it) }
                .firstOrNull { r ->
                    runCatching { java.time.Instant.parse(r.optString("created_at")).toEpochMilli() >= since - 60_000 }
                        .getOrDefault(false)
                }
                ?.let { r -> RunStatus(done = r.optString("status") == "completed", ok = r.optString("conclusion") == "success") }
        } finally {
            conn.disconnect()
        }
    }

    /**
     * Geçmiş bültenler (yerel arama için). Telefonda kopyası olan eski bültenler tekrar indirilmez;
     * yayınlandıktan sonra değişmezler. Sadece en son bülten her seferinde yeniden alınır.
     */
    suspend fun allBulletins(refs: List<BulletinRef>): List<Bulletin> = withContext(Dispatchers.IO) {
        val oldest = oldestKept()
        refs.filter { oldest == null || it.date >= oldest }.mapIndexedNotNull { i, ref ->
            val file = File(cache, "b_${ref.date}.json")
            runCatching {
                if (i > 0 && file.exists()) parseBulletin(file.readText()) else bulletin(ref.date).value
            }.getOrNull()
        }
    }

    /** Telefonda bülten saklama süresi (gün). null = sınırsız. Sitede ayrıca 2 yıl saklanır. */
    var retentionDays: Int?
        get() = prefs.getInt("saklama_gun", 0).takeIf { it > 0 }
        set(value) {
            prefs.edit().putInt("saklama_gun", value ?: 0).apply()
            bulletinCache = null
            pruneCache()
        }

    /** Saklanan en eski bülten günü (YYYY-AA-GG) veya sınırsızsa null. */
    fun oldestKept(): String? = retentionDays?.let {
        java.time.LocalDate.now(java.time.ZoneId.of("Europe/Istanbul")).minusDays(it.toLong()).toString()
    }

    /** Saklama süresini aşan bülten kopyalarını telefondan siler. */
    fun pruneCache() {
        val oldest = oldestKept() ?: return
        cache.listFiles()?.filter {
            it.name.startsWith("b_") && it.name.removePrefix("b_").removeSuffix(".json") < oldest
        }?.forEach { it.delete() }
    }

    /**
     * "Günlük bülten (DeepSeek, elle)" iş akışını başlatır (ücretli, yaklaşık 0,08 dolar).
     * date verilirse sadece o günün haberleri; null ise dün ve bugünün haberleri, bülten bugünün tarihini alır.
     */
    suspend fun runBulletin(date: String?) = dispatch("daily.yml", mapOf("date" to (date ?: "")))

    /** Ara ekranı için bellekte tutulan bülten listesi (uygulama açık kaldıkça tekrar okunmaz). */
    @Volatile var bulletinCache: List<Bulletin>? = null

    /** Uygulamada açılıp görülen en son bülten; bildirim aynı bülten için tekrar gelmesin diye. */
    var lastSeenDate: String
        get() = prefs.getString("son_gorulen", "") ?: ""
        set(value) { if (value > lastSeenDate) prefs.edit().putString("son_gorulen", value).apply() }

    var lastNotifiedDate: String
        get() = prefs.getString("son_bildirilen", "") ?: ""
        set(value) = prefs.edit().putString("son_bildirilen", value).apply()

    var notificationsOn: Boolean
        get() = prefs.getBoolean("bildirim", true)
        set(value) = prefs.edit().putBoolean("bildirim", value).apply()

    /** GitHub ince ayarlı erişim anahtarı (sadece haber-bulteni deposu, Actions: okuma ve yazma). */
    var githubToken: String
        get() = prefs.getString("github_token", "") ?: ""
        set(value) = prefs.edit().putString("github_token", value.trim()).apply()

    /** Başlatılmış ama sonucu henüz sitede görünmeyen araştırmalar: sorgu -> başlatılma zamanı (ms). */
    fun pendingResearch(): Map<String, Long> =
        prefs.getStringSet("bekleyen_arastirma", emptySet())!!.mapNotNull { line ->
            val i = line.indexOf('|')
            if (i < 0) null else line.substring(i + 1) to (line.substring(0, i).toLongOrNull() ?: 0L)
        }.toMap()

    fun setPendingResearch(map: Map<String, Long>) {
        prefs.edit().putStringSet("bekleyen_arastirma", map.map { (q, t) -> "$t|$q" }.toSet()).apply()
    }

    /** GitHub Actions'taki "Araştırma (uygulamadan)" iş akışını başlatır. */
    suspend fun startResearch(query: String) {
        dispatch("research.yml", mapOf("query" to query))
        setPendingResearch(pendingResearch() + (query to System.currentTimeMillis()))
    }

    /**
     * Konularım'daki "Kaydet": tüm değişiklikler tek JSON ile "Konu değişikliği (uygulamadan)" iş akışına gider
     * (bulten/topics.py save_all). Böylece art arda birçok iş başlatılmaz.
     */
    suspend fun saveTopics(changes: String) =
        dispatch("topics.yml", mapOf("action" to "save", "text" to changes))

    /** Ana sayfadaki başlık adları ve gruplar için son okunan konu listesi. */
    @Volatile var topicsCache: Topics? = null

    private suspend fun dispatch(workflow: String, inputs: Map<String, String>) = withContext(Dispatchers.IO) {
        val token = githubToken
        if (token.isBlank()) error("Önce Ayarlar'dan GitHub erişim anahtarını gir.")
        val conn = URL("https://api.github.com/repos/$REPO/actions/workflows/$workflow/dispatches")
            .openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "POST"
            conn.connectTimeout = 15_000
            conn.readTimeout = 20_000
            conn.doOutput = true
            conn.setRequestProperty("Accept", "application/vnd.github+json")
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
            conn.setRequestProperty("Content-Type", "application/json")
            val ins = org.json.JSONObject()
            inputs.forEach { (k, v) -> if (v.isNotEmpty()) ins.put(k, v) }
            val body = org.json.JSONObject().put("ref", "main").put("inputs", ins).toString()
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = conn.responseCode
            if (code != 204) {
                val msg = conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                error(when (code) {
                    401 -> "Erişim anahtarı geçersiz veya süresi dolmuş."
                    403, 404 -> "Anahtarın bu depoda Actions yazma izni yok (veya iş akışı henüz GitHub'da değil)."
                    422 -> "GitHub iş akışını kabul etmedi (iş akışı dosyasında hata olabilir)."
                    else -> "GitHub yanıtı $code ${msg.take(200)}"
                })
            }
        } finally {
            conn.disconnect()
        }
    }

    var themeMode: ThemeMode
        get() = prefs.getString("tema", null)?.let { n -> ThemeMode.entries.firstOrNull { it.name == n } } ?: ThemeMode.SISTEM
        set(value) = prefs.edit().putString("tema", value.name).apply()

    /** Sunucu bülteni zaten konu seviyelerine göre hazırlıyor; burada ek filtre yok. */
    fun visible(topic: Topic): List<Story> = topic.stories

    private suspend fun <T> load(path: String, parse: (String) -> T, fresh: Boolean = false): Loaded<T> = withContext(Dispatchers.IO) {
        val file = File(cache, path.replace('/', '_'))
        try {
            val text = fetch(path, fresh)
            val value = parse(text)  // önce çözümle, bozuk veriyle önbelleği ezme
            file.writeText(text)
            Loaded(value, offline = false)
        } catch (e: Exception) {
            if (file.exists()) Loaded(parse(file.readText()), offline = true) else throw e
        }
    }

    private fun fetch(path: String, fresh: Boolean = false): String {
        // GitHub Pages yanıtları birkaç dakika önbellekte tutar; dakikalık parametre bunu aşar.
        // fresh: her istekte farklı parametre (kaydetmeden sonra yeni hali beklerken, aynı dakika içinde de taze).
        val key = if (fresh) System.currentTimeMillis() else System.currentTimeMillis() / 60_000
        val url = URL("$BASE$path?t=$key")
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
        const val REPO = "marjinalbaykus/haber-bulteni"
        const val BASE = "https://marjinalbaykus.github.io/haber-bulteni/api/"
    }
}
