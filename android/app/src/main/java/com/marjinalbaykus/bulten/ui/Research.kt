package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.data.Research
import com.marjinalbaykus.bulten.data.ResearchRef
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Bir araştırmanın sonucu siteye düşmezse bu süreden sonra bekleyenlerden çıkarılır. */
private const val PENDING_TIMEOUT_MS = 30 * 60_000L

@Composable
fun SearchScreen(repo: Repo, onOpen: (String) -> Unit, onSettings: () -> Unit, padding: PaddingValues) {
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    var query by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var list by remember { mutableStateOf<List<ResearchRef>?>(null) }
    var pending by remember { mutableStateOf(repo.pendingResearch()) }
    val hasToken = remember { repo.githubToken.isNotBlank() }

    var pollKey by remember { mutableIntStateOf(0) }

    // Liste yüklenir; bekleyen araştırma varsa sonucu sitede görünene kadar 30 saniyede bir yeniden bakılır.
    // Yeni araştırma başlatılınca pollKey artar ve döngü baştan kurulur.
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

    fun submit() {
        val q = query.trim()
        if (q.length < 3 || sending) return
        keyboard?.hide()
        sending = true
        message = null
        scope.launch {
            try {
                repo.startResearch(q)
                pending = repo.pendingResearch()
                query = ""
                message = "Başlatıldı. Sonuç genelde 3-6 dakikada burada çıkar; ekran açıkken kendiliğinden yenilenir."
                pollKey++
            } catch (e: Exception) {
                message = e.message ?: e.javaClass.simpleName
            }
            sending = false
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                "Merak ettiğin bir konuyu yaz; haber kaynakları taranıp Türkçe bir rapor hazırlanır.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Örn: RX 7800 XT için en iyi yerel modeller") },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { submit() }),
                enabled = hasToken && !sending,
                minLines = 1,
                maxLines = 4,
            )
            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (sending) CircularProgressIndicator(Modifier.padding(end = 12.dp).size(20.dp), strokeWidth = 2.dp)
                Button(onClick = { submit() }, enabled = hasToken && !sending && query.trim().length >= 3) {
                    Text("Araştır")
                }
            }
            if (!hasToken) {
                Text(
                    "Araştırma başlatmak için Ayarlar'dan GitHub erişim anahtarını girmen gerekiyor.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 6.dp),
                )
                Text("Ayarlara git", color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSettings() }.padding(vertical = 6.dp))
            }
            message?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
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
        item { SectionTitle("Önceki araştırmalar") }
        val l = list
        when {
            l == null -> item { Centered { CircularProgressIndicator() } }
            l.isEmpty() -> item {
                Text("Henüz araştırma yok.", fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            else -> items(l, key = { it.id }) { r ->
                Column(Modifier.fillMaxWidth().clickable { onOpen(r.id) }.padding(vertical = 10.dp)) {
                    Text(r.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("${r.created} · ${r.query}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
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
        r == null && error != null -> ErrorView(error!!) { attempt++ }
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
    }
}
