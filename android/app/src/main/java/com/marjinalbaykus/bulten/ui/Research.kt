package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.data.Research
import com.marjinalbaykus.bulten.data.ResearchRef
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Bir araştırmanın sonucu siteye düşmezse bu süreden sonra bekleyenlerden çıkarılır. */
private const val PENDING_TIMEOUT_MS = 30 * 60_000L

/** Türkçe harf ve büyük/küçük farkını yok sayan arama için: "Güler" ile "guler" eşleşir. */
private fun norm(s: String): String {
    val sb = StringBuilder(s.length)
    for (c in s.lowercase(java.util.Locale.forLanguageTag("tr-TR"))) {
        sb.append(
            when (c) {
                'ç' -> 'c'; 'ğ' -> 'g'; 'ı' -> 'i'; 'ö' -> 'o'; 'ş' -> 's'; 'ü' -> 'u'; 'â' -> 'a'; 'î' -> 'i'; 'û' -> 'u'
                else -> c
            },
        )
    }
    return sb.toString()
}

data class SearchHit(val date: String, val item: Item)

/** Her kelime, metinde bir kelimenin başında geçmeli: "arda" -> "Arda", "Arda'nın" eşleşir, "katlarda" eşleşmez. */
private fun wordMatchers(query: String): List<Regex> =
    norm(query).split(' ').map { it.trim() }.filter { it.length >= 2 }
        .map { Regex("""(^|[^\p{L}\p{N}])""" + Regex.escape(it)) }

/** Tüm kelimeleri içeren haberler; yeni tarihli ve önemli olanlar önce. */
private fun searchBulletins(bulletins: List<Bulletin>, query: String): List<SearchHit> {
    val words = wordMatchers(query)
    if (words.isEmpty()) return emptyList()
    val hits = mutableListOf<SearchHit>()
    for (b in bulletins) for (t in b.topics) for (st in t.stories) {
        val hay = norm(listOf(t.title, st.title, st.headline, st.detail, st.keyPoints.joinToString(" ")).joinToString(" "))
        if (words.all { it.containsMatchIn(hay) }) hits += SearchHit(b.date, Item(t, st))
    }
    return hits.sortedWith(compareByDescending<SearchHit> { it.date }.thenByDescending { it.item.story.importance })
}

@Composable
fun SearchScreen(
    repo: Repo,
    query: String,
    onQuery: (String) -> Unit,
    onOpenResearch: (String) -> Unit,
    onOpenItem: (Item) -> Unit,
    onSettings: () -> Unit,
    padding: PaddingValues,
) {
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    var sending by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var list by remember { mutableStateOf<List<ResearchRef>?>(null) }
    var pending by remember { mutableStateOf(repo.pendingResearch()) }
    var bulletins by remember { mutableStateOf(repo.bulletinCache) }
    val hasToken = remember { repo.githubToken.isNotBlank() }
    var pollKey by remember { mutableIntStateOf(0) }

    // Geçmiş bültenler bir kez indirilir (eskiler telefonda saklanır), arama tamamen telefonda yapılır.
    LaunchedEffect(Unit) {
        if (bulletins == null) {
            runCatching { repo.allBulletins(repo.index().value.bulletins) }.onSuccess {
                repo.bulletinCache = it
                bulletins = it
            }.onFailure { bulletins = emptyList() }
        }
    }

    // Araştırma listesi; bekleyen varsa sonucu sitede görünene kadar 30 saniyede bir yeniden bakılır.
    LaunchedEffect(pollKey) {
        while (true) {
            runCatching { repo.index() }.onSuccess { idx ->
                val refs = idx.value.research.sortedByDescending { it.created }
                list = refs
                val done = refs.map { it.query.trim().lowercase() }.toSet()
                val now = System.currentTimeMillis()
                val still = repo.pendingResearch()
                    .filter { (q, t) -> q.trim().lowercase() !in done && now - t < PENDING_TIMEOUT_MS }
                repo.setPendingResearch(still)
                pending = still
            }.onFailure { if (list == null) list = emptyList() }
            if (pending.isEmpty()) break
            delay(30_000)
        }
    }

    fun research() {
        val q = query.trim()
        if (q.length < 3 || sending) return
        keyboard?.hide()
        sending = true
        message = null
        scope.launch {
            try {
                repo.startResearch(q)
                pending = repo.pendingResearch()
                onQuery("")
                message = "Araştırma başlatıldı. Sonuç genelde 3-4 dakikada aşağıda çıkar; ekran açıkken kendiliğinden yenilenir."
                pollKey++
            } catch (e: Exception) {
                message = e.message ?: e.javaClass.simpleName
            }
            sending = false
        }
    }

    val hits = remember(query, bulletins) { bulletins?.let { searchBulletins(it, query) } ?: emptyList() }
    val searching = query.trim().length >= 2
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { onQuery(it); message = null },
                placeholder = { Text("Örn: Arda Güler") },
                modifier = Modifier.keepAboveKeyboard().fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                singleLine = true,
            )
            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Açıklama "!" simgesinin arkasında; dokununca açılır
                Box(
                    Modifier.size(26.dp).clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { info = !info },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge)
                }
                Spacer(Modifier.weight(1f))
                if (sending) CircularProgressIndicator(Modifier.padding(horizontal = 8.dp).size(18.dp), strokeWidth = 2.dp)
                FilledTonalButton(
                    onClick = { research() },
                    enabled = hasToken && !sending && query.trim().length >= 3,
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text("Araştır") }
            }
            if (info) {
                Text(
                    "Yazdıkça geçmiş bültenlerde arar. Daha fazlası için Araştır: haber kaynakları taranıp Türkçe " +
                        "rapor hazırlanır. Bir araştırma ortalama 3-4 dakika sürer ve yaklaşık 0,005 dolar tutar (DeepSeek). " +
                        "Ekran açıkken sonuç kendiliğinden aşağıda çıkar.",
                    style = MaterialTheme.typography.bodySmall, color = muted, modifier = Modifier.padding(top = 6.dp),
                )
            }
            if (!hasToken) {
                Text("Araştır için Ayarlar'dan GitHub erişim anahtarı girilmeli. Ayarlara git",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSettings() }.padding(vertical = 6.dp))
            }
            message?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
            }
        }
        if (searching) {
            item {
                SectionTitle(
                    when {
                        bulletins == null -> "Bültenler yükleniyor..."
                        hits.isEmpty() -> "Bültenlerde sonuç yok"
                        else -> "Bültenlerde ${hits.size} sonuç"
                    },
                )
            }
            items(hits.take(100), key = { "${it.date}-${it.item.story.sid}" }) { h ->
                Column(Modifier.fillMaxWidth().clickable { onOpenItem(h.item) }.padding(vertical = 9.dp)) {
                    Text("${trDate(h.date, short = true)} · ${h.item.topic.title}",
                        style = MaterialTheme.typography.labelSmall, color = muted)
                    Text(h.item.story.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(h.item.story.headline, style = MaterialTheme.typography.bodySmall, color = muted, maxLines = 2)
                }
                HorizontalDivider()
            }
        }
        if (pending.isNotEmpty()) {
            item { SectionTitle("Hazırlanıyor") }
            items(pending.keys.toList()) { q ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                    Text(q, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
        val l = list
        val shownResearch = if (searching && l != null) {
            val words = wordMatchers(query)
            l.filter { r -> norm(r.title + " " + r.query).let { h -> words.all { it.containsMatchIn(h) } } }
        } else l
        if (!searching || !shownResearch.isNullOrEmpty()) {
            item { SectionTitle(if (searching) "Araştırmalarda" else "Önceki araştırmalar") }
        }
        when {
            shownResearch == null -> item { Centered { CircularProgressIndicator() } }
            shownResearch.isEmpty() && !searching -> item {
                Text("Henüz araştırma yok.", fontStyle = FontStyle.Italic, color = muted)
            }
            else -> items(shownResearch, key = { it.id }) { r ->
                Column(Modifier.fillMaxWidth().clickable { onOpenResearch(r.id) }.padding(vertical = 10.dp)) {
                    Text(r.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("${r.created} · ${r.query}", style = MaterialTheme.typography.bodySmall,
                        color = muted, maxLines = 2)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ResearchScreen(repo: Repo, id: String, padding: PaddingValues) {
    var research by remember { mutableStateOf<Research?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var attempt by remember { mutableIntStateOf(0) }
    LaunchedEffect(id, attempt) {
        error = null
        try {
            research = repo.research(id).value
        } catch (e: Exception) {
            error = e.message ?: e.javaClass.simpleName
        }
    }
    val r = research
    when {
        r == null && error != null -> ErrorView(error!!, "Araştırma yüklenemedi") { attempt++ }
        r == null -> Centered { CircularProgressIndicator() }
        else -> ResearchBody(r, padding)
    }
}

@Composable
private fun ResearchBody(r: Research, padding: PaddingValues) {
    val uri = LocalUriHandler.current
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
        Text(r.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text("${r.created} · \"${r.query}\"", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
        if (r.summaryPoints.isNotEmpty()) {
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Özet", fontWeight = FontWeight.Bold)
                    r.summaryPoints.forEach {
                        Text("• $it", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        r.sections.forEach { s ->
            Text(s.heading, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 14.dp, bottom = 4.dp))
            s.body.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }.forEach { p ->
                Text(p, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
            }
        }
        if (r.caveats.isNotBlank()) {
            Text(r.caveats, fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
        }
        if (r.sources.isNotEmpty()) {
            SectionTitle("Kaynaklar (${r.sources.size})")
            r.sources.forEach { src ->
                Column(
                    Modifier.fillMaxWidth().clickable { if (src.url.isNotBlank()) uri.openUri(src.url) }
                        .padding(vertical = 7.dp),
                ) {
                    Text(src.title, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
                    Text("${src.lang.uppercase()} · ${src.source}", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        r.stats?.let { st ->
            HorizontalDivider(Modifier.padding(top = 16.dp, bottom = 8.dp))
            val model = if (st.model.startsWith("deepseek")) "DeepSeek (${st.model})" else st.model
            val total = st.promptTokens + st.completionTokens
            Text(
                "Model: $model\nToken: ${trInt(total)} (giriş ${trInt(st.promptTokens)}, " +
                    "çıkış ${trInt(st.completionTokens)})" +
                    (st.costUsd?.let { "\nMaliyet: yaklaşık ${String.format(java.util.Locale.forLanguageTag("tr-TR"), "%.4f", it)} dolar" } ?: ""),
                style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun trInt(n: Int): String =
    java.text.NumberFormat.getIntegerInstance(java.util.Locale.forLanguageTag("tr-TR")).format(n)
