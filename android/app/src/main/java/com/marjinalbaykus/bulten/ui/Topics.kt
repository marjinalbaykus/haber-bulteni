package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.data.Level
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.data.TopicInfo
import com.marjinalbaykus.bulten.data.Topics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LevelPicker(selected: Level, enabled: Boolean, onPick: (Level) -> Unit, modifier: Modifier = Modifier) {
    SingleChoiceSegmentedButtonRow(modifier) {
        Level.entries.forEachIndexed { i, lv ->
            SegmentedButton(
                selected = lv == selected,
                onClick = { if (lv != selected) onPick(lv) },
                enabled = enabled,
                shape = SegmentedButtonDefaults.itemShape(i, Level.entries.size),
                icon = {},
                label = { Text(lv.label, style = MaterialTheme.typography.labelMedium) },
            )
        }
    }
}

/**
 * Konularım: konu ekleme/kaldırma ve her konu için Az/Orta/Çok.
 * Her değişiklik GitHub'daki topics.yml iş akışını başlatır; sunucu 1-2 dakikada işler ve
 * ertesi sabahki bülten yeni ayarla hazırlanır. Ekranda değişiklik hemen gösterilir.
 */
@Composable
fun TopicsScreen(repo: Repo, onSettings: () -> Unit, padding: PaddingValues) {
    val scope = rememberCoroutineScope()
    val hasToken = remember { repo.githubToken.isNotBlank() }
    var topics by remember { mutableStateOf<Topics?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var reload by remember { mutableIntStateOf(0) }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var confirmRemove by remember { mutableStateOf<TopicInfo?>(null) }
    var newText by remember { mutableStateOf("") }
    var newLevel by remember { mutableStateOf(Level.ORTA) }
    var adding by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(reload) {
        runCatching { repo.topics().value }
            .onSuccess { topics = it; error = null }
            .onFailure { if (topics == null) error = it.message ?: it.javaClass.simpleName }
    }

    /** İş akışını başlatır; başarılıysa ekranı hemen günceller, 2 dakika sonra sunucudan tekrar okur. */
    fun send(done: String, optimistic: (Topics) -> Topics, call: suspend () -> Unit) {
        if (busy) return
        busy = true
        message = null
        scope.launch {
            try {
                call()
                topics = topics?.let(optimistic)
                message = "$done Sunucuya 1-2 dakikada işlenir, yarınki bültenden itibaren geçerli olur."
                launch { delay(120_000); reload++ }
            } catch (e: Exception) {
                message = e.message ?: e.javaClass.simpleName
            }
            busy = false
        }
    }

    val t = topics
    if (t == null) {
        if (error != null) ErrorView(error!!, "Konular yüklenemedi") { reload++ } else Centered { CircularProgressIndicator() }
        return
    }
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val enabled = hasToken && !busy

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                "Az: sadece çok önemli (en fazla ${Level.AZ.maxStories}), Orta: önemli (en fazla ${Level.ORTA.maxStories}), " +
                    "Çok: daha geniş (en fazla ${Level.COK.maxStories}) haber.",
                style = MaterialTheme.typography.bodySmall, color = muted,
            )
            if (!hasToken) {
                Text("Değişiklik yapmak için Ayarlar'dan GitHub erişim anahtarı girilmeli. Ayarlara git",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSettings() }.padding(vertical = 6.dp))
            }
            message?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
            }
        }
        items(t.active, key = { it.id }) { topic ->
            Column(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(topic.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f))
                    TextButton(onClick = { confirmRemove = topic }, enabled = enabled) { Text("Kaldır") }
                }
                LevelPicker(topic.level, enabled, { lv ->
                    send("${topic.title}: ${lv.label}.",
                        { ts -> ts.copy(active = ts.active.map { if (it.id == topic.id) it.copy(level = lv) else it }) },
                    ) { repo.changeTopic("level", topic.id, level = lv) }
                }, Modifier.fillMaxWidth().padding(top = 4.dp))
            }
            HorizontalDivider()
        }
        adding?.let { a ->
            item {
                Text("Ekleniyor: $a", fontStyle = FontStyle.Italic, color = muted,
                    modifier = Modifier.padding(vertical = 10.dp))
            }
        }
        item {
            SectionTitle("Yeni konu ekle")
            Text(
                "Takip etmek istediğin konuyu birkaç kelimeyle anlat. Arama sorguları ve neyin önemli olduğu " +
                    "otomatik hazırlanır.",
                style = MaterialTheme.typography.bodySmall, color = muted,
            )
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it.take(300) },
                placeholder = { Text("Örn: Formula 1, özellikle Ferrari ve yeni kurallar") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = enabled,
                maxLines = 4,
            )
            LevelPicker(newLevel, enabled, { newLevel = it }, Modifier.fillMaxWidth().padding(top = 8.dp))
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End) {
                FilledTonalButton(
                    onClick = {
                        val text = newText.trim()
                        send("Yeni konu isteği gönderildi.", { it }) {
                            repo.changeTopic("add", text = text, level = newLevel)
                            adding = text
                            newText = ""
                        }
                    },
                    enabled = enabled && newText.trim().length >= 2,
                ) { Text("Ekle") }
            }
        }
        if (t.removed.isNotEmpty()) {
            item { SectionTitle("Kaldırılanlar") }
            items(t.removed, key = { "r-${it.id}" }) { topic ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(topic.title, style = MaterialTheme.typography.bodyMedium, color = muted, modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        send("${topic.title} geri eklendi.",
                            { ts -> ts.copy(active = ts.active + topic, removed = ts.removed - topic) },
                        ) { repo.changeTopic("restore", topic.id) }
                    }, enabled = enabled) { Text("Geri ekle") }
                }
            }
        }
    }

    confirmRemove?.let { topic ->
        AlertDialog(
            onDismissRequest = { confirmRemove = null },
            title = { Text("Konu kaldırılsın mı?") },
            text = {
                Text(
                    "\"${topic.title}\" yarından itibaren bültende olmaz." +
                        if (topic.custom) " Sonradan eklenen bir konu olduğu için tamamen silinir." else
                            " İstersen \"Kaldırılanlar\" bölümünden geri ekleyebilirsin.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmRemove = null
                    send("${topic.title} kaldırıldı.",
                        { ts ->
                            ts.copy(active = ts.active - topic,
                                removed = if (topic.custom) ts.removed else ts.removed + topic)
                        },
                    ) { repo.changeTopic("remove", topic.id) }
                }) { Text("Kaldır") }
            },
            dismissButton = { TextButton(onClick = { confirmRemove = null }) { Text("Vazgeç") } },
        )
    }
}
