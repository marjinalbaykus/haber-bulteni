package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.data.Level
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.data.TopicGroup
import com.marjinalbaykus.bulten.data.TopicInfo
import com.marjinalbaykus.bulten.data.Topics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LevelPicker(selected: Level, onPick: (Level) -> Unit, modifier: Modifier = Modifier) {
    SingleChoiceSegmentedButtonRow(modifier) {
        Level.entries.forEachIndexed { i, lv ->
            SegmentedButton(
                selected = lv == selected,
                onClick = { onPick(lv) },
                shape = SegmentedButtonDefaults.itemShape(i, Level.entries.size),
                icon = {},
                label = { Text(lv.label, style = MaterialTheme.typography.labelMedium) },
            )
        }
    }
}

/** Kaydedilmeyi bekleyen yeni konu. */
private data class NewTopic(val text: String, val level: Level, val group: String?)

/**
 * Kaydedilmemiş değişiklikler. Uygulama seviyesinde tutulur: başka sekmeye geçip dönünce kaybolmaz,
 * kaydetme başarısız olursa da yerinde kalır.
 */
class TopicsDraft {
    internal val levels = mutableStateMapOf<String, Level>()
    internal val titles = mutableStateMapOf<String, String>()
    internal val removeIds = mutableStateListOf<String>()
    internal val restoreIds = mutableStateListOf<String>()
    internal val adds = mutableStateListOf<Any>()   // NewTopic
    internal var groups by mutableStateOf<List<TopicGroup>?>(null)

    // Kaydedildi, sunucunun işlemesi bekleniyor: before = kaydetmeden önceki sunucu listesi,
    // shown = ekranda gösterilen (kaydedilmiş varsayılan) liste, waiting = eklenmekte olan konular
    internal var before by mutableStateOf<Topics?>(null)
    internal var shown by mutableStateOf<Topics?>(null)
    internal var savedAt = 0L
    internal var waiting by mutableStateOf<List<String>>(emptyList())
}

/** Ad sorma penceresi (konu adı, grup adı, yeni grup). */
private data class NameAsk(val title: String, val initial: String, val onDone: (String) -> Unit)

/**
 * Konularım: başlıklar gruplar halinde. Seviye, ad, grup, kaldırma ve yeni konu önce ekranda taslak olarak
 * değişir; alttaki sabit "Kaydet" hepsini tek seferde GitHub'daki topics.yml iş akışına gönderir.
 */
@Composable
fun TopicsScreen(repo: Repo, draft: TopicsDraft, onSettings: () -> Unit, onSaved: (Topics) -> Unit, padding: PaddingValues) {
    val scope = rememberCoroutineScope()
    var server by remember { mutableStateOf<Topics?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var reload by remember { mutableIntStateOf(0) }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    // Taslak
    val levels = draft.levels
    val titles = draft.titles
    val removeIds = draft.removeIds
    val restoreIds = draft.restoreIds
    @Suppress("UNCHECKED_CAST") val adds = draft.adds as MutableList<NewTopic>
    var groupsDraft by draft::groups
    var waiting by draft::waiting

    var addOpen by remember { mutableStateOf(false) }
    var newText by remember { mutableStateOf("") }
    var newLevel by remember { mutableStateOf(Level.ORTA) }
    var newGroup by remember { mutableStateOf<String?>(null) }
    var ask by remember { mutableStateOf<NameAsk?>(null) }
    var confirmGroupDelete by remember { mutableStateOf<TopicGroup?>(null) }
    var confirmRemove by remember { mutableStateOf<TopicInfo?>(null) }
    var failed by remember { mutableStateOf<String?>(null) }
    var savedNote by remember { mutableStateOf(false) }   // "Kaydedildi" birkaç saniye görünür
    LaunchedEffect(savedNote) { if (savedNote) { delay(5_000); savedNote = false } }   // son kaydetme hatası (alt çubukta gösterilir)

    // Sunucudan oku. Kaydetme bekleniyorsa sunucudaki liste değişene kadar 20 saniyede bir tekrar bakar
    // (GitHub'daki iş ve site güncellemesi genelde 2-3 dakika sürer), değişince ekranı ve ana sayfayı günceller.
    LaunchedEffect(reload) {
        if (server == null) draft.shown?.let { server = it }
        val t = runCatching { repo.topics().value }
            .onFailure { if (server == null) error = it.message ?: it.javaClass.simpleName }
            .getOrNull()
        val before = draft.before
        when {
            before == null -> if (t != null) {
                server = t
                error = null
                repo.topicsCache = t
            }
            t != null && t != before -> {
                server = t
                repo.topicsCache = t
                onSaved(t)
                draft.before = null
                draft.shown = null
                waiting = emptyList()
                message = null
                savedNote = true
            }
            System.currentTimeMillis() - draft.savedAt < 10 * 60_000L -> {
                delay(20_000)
                reload++
            }
            else -> {
                draft.before = null
                message = "Sunucudan henüz sonuç gelmedi. Biraz sonra Konular'a tekrar girince güncellenir."
            }
        }
    }

    val s = server
    if (s == null) {
        if (error != null) ErrorView(error!!, "Konular yüklenemedi") { reload++ } else Centered { CircularProgressIndicator() }
        return
    }

    // Taslağın uygulanmış hali
    val groups = groupsDraft ?: s.groups
    val active = s.active.filter { it.id !in removeIds } + s.removed.filter { it.id in restoreIds }
    val removed = s.removed.filter { it.id !in restoreIds } + s.active.filter { it.id in removeIds }
    fun levelOf(t: TopicInfo) = levels[t.id] ?: t.level
    fun titleOf(t: TopicInfo) = titles[t.id] ?: t.title
    val changeCount = levels.count { (id, lv) -> s.active.firstOrNull { it.id == id }?.level != lv } +
        titles.size + removeIds.size + restoreIds.size + adds.size + (if (groupsDraft != null) 1 else 0)
    val dirty = changeCount > 0

    fun editGroups(f: (MutableList<TopicGroup>) -> Unit) {
        groupsDraft = groups.toMutableList().also(f)
    }
    fun moveTo(topicId: String, groupId: String?) = editGroups { gs ->
        for (i in gs.indices) gs[i] = gs[i].copy(topics = gs[i].topics - topicId)
        val k = gs.indexOfFirst { it.id == groupId }
        if (k >= 0) gs[k] = gs[k].copy(topics = gs[k].topics + topicId)
    }
    fun clearDraft() {
        levels.clear(); titles.clear(); removeIds.clear(); restoreIds.clear(); adds.clear(); groupsDraft = null
    }

    fun save() {
        if (repo.githubToken.isBlank()) {
            failed = "Kaydetmek için Ayarlar'dan GitHub erişim anahtarı girilmeli."
            return
        }
        val o = JSONObject()
        val lv = JSONObject()
        levels.forEach { (id, l) -> lv.put(id, l.key) }
        if (lv.length() > 0) o.put("levels", lv)
        if (titles.isNotEmpty()) o.put("titles", JSONObject(titles.toMap()))
        if (removeIds.isNotEmpty()) o.put("remove", JSONArray(removeIds.toList()))
        if (restoreIds.isNotEmpty()) o.put("restore", JSONArray(restoreIds.toList()))
        if (adds.isNotEmpty()) o.put("add", JSONArray(adds.map {
            JSONObject().put("text", it.text).put("level", it.level.key).apply { it.group?.let { g -> put("group", g) } }
        }))
        // Grup listesi her zaman gönderilir: geri eklenen veya yeni konular doğru gruba girsin
        o.put("groups", JSONArray(groups.map { g ->
            JSONObject().put("id", g.id).put("name", g.name).put("topics", JSONArray(g.topics))
        }))
        busy = true
        message = null
        failed = null
        scope.launch {
            try {
                repo.saveTopics(o.toString())
                // Ekranda hemen göster; sunucu 1-2 dakikada işler
                val now = Topics(
                    active = active.map { it.copy(title = titleOf(it), level = levelOf(it)) },
                    removed = removed.filter { !it.custom },
                    groups = groups.filter { g -> g.topics.any { id -> active.any { it.id == id } } || g.topics.isEmpty() },
                )
                waiting = waiting + adds.map { it.text }
                if (draft.before == null) draft.before = s
                draft.shown = now
                draft.savedAt = System.currentTimeMillis()
                server = now
                repo.topicsCache = now
                onSaved(now)
                clearDraft()
                message = null
                reload++
            } catch (e: Exception) {
                failed = e.message ?: e.javaClass.simpleName
            }
            busy = false
        }
    }

    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val grouped = groups.flatMap { it.topics }.toSet()
    val ungrouped = active.filter { it.id !in grouped }

    @Composable
    fun TopicRow(topic: TopicInfo, groupId: String?) {
        var menu by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth().padding(start = if (groupId != null) 8.dp else 0.dp, top = 8.dp, bottom = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(titleOf(topic), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { menu = true }) { Text("⋮", style = MaterialTheme.typography.titleLarge) }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Adını değiştir") }, onClick = {
                            menu = false
                            ask = NameAsk("Başlık adı", titleOf(topic)) { name ->
                                if (name == topic.title) titles.remove(topic.id) else titles[topic.id] = name
                            }
                        })
                        groups.filter { it.id != groupId }.forEach { g ->
                            DropdownMenuItem(text = { Text("${g.name} grubuna taşı") }, onClick = {
                                menu = false
                                moveTo(topic.id, g.id)
                            })
                        }
                        if (groupId != null) {
                            DropdownMenuItem(text = { Text("Gruptan çıkar") }, onClick = {
                                menu = false
                                moveTo(topic.id, null)
                            })
                        }
                        DropdownMenuItem(text = { Text("Kaldır") }, onClick = {
                            menu = false
                            confirmRemove = topic
                        })
                    }
                }
            }
            LevelPicker(levelOf(topic), { lv ->
                if (lv == topic.level) levels.remove(topic.id) else levels[topic.id] = lv
            }, Modifier.fillMaxWidth().padding(top = 2.dp))
        }
    }

    Column(Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            modifier = Modifier.weight(1f),
        ) {
            item {
                Text(
                    "Az: sadece çok önemli (en fazla ${Level.AZ.maxStories}), Orta: önemli (en fazla ${Level.ORTA.maxStories}), " +
                        "Çok: daha geniş (en fazla ${Level.COK.maxStories}) haber. Gruplar sadece ana sayfadaki görünümü belirler.",
                    style = MaterialTheme.typography.bodySmall, color = muted,
                )
                if (repo.githubToken.isBlank()) {
                    Text("Kaydetmek için Ayarlar'dan GitHub erişim anahtarı girilmeli. Ayarlara git",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onSettings() }.padding(vertical = 6.dp))
                }
                message?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
                }
            }
            // Yeni konu ekle: en üstte, açılır kart
            item {
                Card(
                    Modifier.fillMaxWidth().padding(top = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            (if (addOpen) "Yeni konu ekle" else "+ Yeni konu ekle"),
                            style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth().clickable { addOpen = !addOpen },
                        )
                        if (addOpen) {
                            Text(
                                "Takip etmek istediğin konuyu birkaç kelimeyle anlat. Arama sorguları ve neyin önemli " +
                                    "olduğu otomatik hazırlanır. Konu listenin sonuna eklenir.",
                                style = MaterialTheme.typography.bodySmall, color = muted, modifier = Modifier.padding(top = 4.dp),
                            )
                            OutlinedTextField(
                                value = newText,
                                onValueChange = { newText = it.take(300) },
                                placeholder = { Text("Örn: Formula 1") },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                maxLines = 4,
                            )
                            LevelPicker(newLevel, { newLevel = it }, Modifier.fillMaxWidth().padding(top = 8.dp))
                            if (groups.isNotEmpty()) {
                                var gm by remember { mutableStateOf(false) }
                                Box(Modifier.padding(top = 4.dp)) {
                                    TextButton(onClick = { gm = true }) {
                                        Text("Grup: " + (groups.firstOrNull { it.id == newGroup }?.name ?: "Grupsuz"))
                                    }
                                    DropdownMenu(expanded = gm, onDismissRequest = { gm = false }) {
                                        DropdownMenuItem(text = { Text("Grupsuz") }, onClick = { gm = false; newGroup = null })
                                        groups.forEach { g ->
                                            DropdownMenuItem(text = { Text(g.name) }, onClick = { gm = false; newGroup = g.id })
                                        }
                                    }
                                }
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                FilledTonalButton(
                                    onClick = {
                                        adds.add(NewTopic(newText.trim(), newLevel, newGroup))
                                        newText = ""
                                        addOpen = false
                                    },
                                    enabled = newText.trim().length >= 2,
                                ) { Text("Listeye ekle") }
                            }
                        }
                    }
                }
            }
            groups.forEach { g ->
                val members = g.topics.mapNotNull { id -> active.firstOrNull { it.id == id } }
                item(key = "g-${g.id}") {
                    Row(Modifier.fillMaxWidth().padding(top = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(g.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            ask = NameAsk("Grup adı", g.name) { name ->
                                editGroups { gs -> gs.replaceAll { if (it.id == g.id) it.copy(name = name) else it } }
                            }
                        }) { Text("Düzenle") }
                        TextButton(onClick = { confirmGroupDelete = g }) { Text("Sil") }
                    }
                    HorizontalDivider()
                    if (members.isEmpty()) {
                        Text("Bu grupta başlık yok. Bir başlığın menüsünden buraya taşıyabilirsin.",
                            style = MaterialTheme.typography.bodySmall, color = muted, modifier = Modifier.padding(8.dp))
                    }
                }
                items(members, key = { "t-${g.id}-${it.id}" }) { TopicRow(it, g.id) }
                adds.filter { it.group == g.id }.forEach { a -> item { PendingAdd(a.text) { adds.remove(a) } } }
            }
            if (ungrouped.isNotEmpty() || adds.any { it.group == null }) {
                item(key = "g-yok") {
                    Text(if (groups.isEmpty()) "Başlıklar" else "Grupsuz",
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 18.dp, bottom = 4.dp))
                    HorizontalDivider()
                }
                items(ungrouped, key = { "t-yok-${it.id}" }) { TopicRow(it, null) }
                adds.filter { it.group == null }.forEach { a -> item { PendingAdd(a.text) { adds.remove(a) } } }
            }
            waiting.forEach { w ->
                item {
                    Text("Ekleniyor: $w", fontStyle = FontStyle.Italic, color = muted, style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 6.dp))
                }
            }
            item {
                TextButton(onClick = {
                    ask = NameAsk("Yeni grup adı", "") { name ->
                        editGroups { gs -> gs.add(TopicGroup("g${System.currentTimeMillis() % 100000}", name, emptyList())) }
                    }
                }, modifier = Modifier.padding(top = 8.dp)) { Text("+ Yeni grup") }
            }
            // Sadece düzenlerken: kaydedince kaldırılacak başlıklar (kaydettikten sonra hiç görünmez)
            val pendingRemove = s.active.filter { it.id in removeIds }
            if (pendingRemove.isNotEmpty()) {
                item { SectionTitle("Kaydedince kaldırılacak") }
                items(pendingRemove, key = { "r-${it.id}" }) { topic ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(titleOf(topic), style = MaterialTheme.typography.bodyMedium, color = muted,
                            textDecoration = TextDecoration.LineThrough, modifier = Modifier.weight(1f))
                        TextButton(onClick = { removeIds.remove(topic.id) }) { Text("Geri al") }
                    }
                }
            }
        }
        // Sabit kaydet çubuğu: liste kaysa da görünür kalır
        Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
            Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 12.dp, top = 8.dp,
                bottom = 8.dp + padding.calculateBottomPadding())) {
                failed?.let {
                    Text("Kaydedilemedi: $it", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val processing = draft.before != null
                    Text(
                        when {
                            busy -> "Gönderiliyor..."
                            dirty -> "$changeCount değişiklik bekliyor"
                            processing -> "Kaydedildi, sunucuda işleniyor..."
                            savedNote -> "Kaydedildi"
                            else -> "Değişiklik yok"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (!dirty && !busy && (processing || savedNote)) MaterialTheme.colorScheme.primary else muted,
                        fontWeight = if (!dirty && !busy && (processing || savedNote)) FontWeight.SemiBold else null,
                        modifier = Modifier.weight(1f),
                    )
                    if (dirty && !busy) TextButton(onClick = { clearDraft(); failed = null }) { Text("Vazgeç") }
                    Button(onClick = { save() }, enabled = dirty && !busy) { Text("Kaydet") }
                }
            }
        }
    }

    ask?.let { a ->
        var value by remember(a) { mutableStateOf(a.initial) }
        AlertDialog(
            onDismissRequest = { ask = null },
            title = { Text(a.title) },
            text = {
                OutlinedTextField(value = value, onValueChange = { value = it.take(60) }, singleLine = true,
                    modifier = Modifier.fillMaxWidth())
            },
            confirmButton = {
                TextButton(onClick = {
                    val v = value.trim()
                    if (v.isNotEmpty()) a.onDone(v)
                    ask = null
                }, enabled = value.isNotBlank()) { Text("Tamam") }
            },
            dismissButton = { TextButton(onClick = { ask = null }) { Text("Vazgeç") } },
        )
    }

    confirmRemove?.let { topic ->
        AlertDialog(
            onDismissRequest = { confirmRemove = null },
            title = { Text("Başlık kaldırılsın mı?") },
            text = {
                Text("\"${titleOf(topic)}\" başlığını kaldırmak istediğine emin misin? Kaydettikten sonra bir sonraki bültenden itibaren " +
                    "bültende olmaz." + if (topic.custom) " Sonradan eklenen bir başlık olduğu için tamamen silinir."
                    else " Kaydetmeden önce geri alabilirsin.")
            },
            confirmButton = {
                TextButton(onClick = {
                    if (topic.id in restoreIds) restoreIds.remove(topic.id) else removeIds.add(topic.id)
                    confirmRemove = null
                }) { Text("Kaldır") }
            },
            dismissButton = { TextButton(onClick = { confirmRemove = null }) { Text("Vazgeç") } },
        )
    }

    confirmGroupDelete?.let { g ->
        AlertDialog(
            onDismissRequest = { confirmGroupDelete = null },
            title = { Text("Grup silinsin mi?") },
            text = { Text("\"${g.name}\" grubu silinir, içindeki başlıklar silinmez, grupsuz kalır.") },
            confirmButton = {
                TextButton(onClick = {
                    editGroups { gs -> gs.removeAll { it.id == g.id } }
                    confirmGroupDelete = null
                }) { Text("Sil") }
            },
            dismissButton = { TextButton(onClick = { confirmGroupDelete = null }) { Text("Vazgeç") } },
        )
    }
}

@Composable
private fun PendingAdd(text: String, onCancel: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("Eklenecek: $text", fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        TextButton(onClick = onCancel) { Text("Vazgeç") }
    }
}
