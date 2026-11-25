package com.marjinalbaykus.bulten.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marjinalbaykus.bulten.BuildConfig
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableIntStateOf
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Index
import com.marjinalbaykus.bulten.data.Story
import com.marjinalbaykus.bulten.data.Topic
import com.marjinalbaykus.bulten.data.Topics
import com.marjinalbaykus.bulten.ui.theme.ThemeMode

/** Bir haber, ait olduğu başlık ve bülten tarihi (okundu bilgisi "tarih/sid" ile tutulur). */
data class Item(val topic: Topic, val story: Story, val date: String = "") {
    val readKey: String get() = "$date/${story.sid}"
}

fun trCount(n: Int): String = java.text.NumberFormat.getIntegerInstance(java.util.Locale.forLanguageTag("tr-TR")).format(n)

/** Başlık adlarını Konularım'daki güncel adlarla değiştirir (eski bültenlerde de yeni ad görünsün). */
fun Bulletin.withTitles(t: Topics?): Bulletin =
    if (t == null) this else copy(topics = topics.map { tp -> t.title(tp.id)?.let { tp.copy(title = it) } ?: tp })

/** Ana sayfadaki bir bölüm: tek başlık ya da bir grup (ör. Futbol). */
private class Section(
    val key: String, val title: String, val items: List<Item>, val note: String, val grouped: Boolean,
    val groupId: String? = null,
)

/** Önce gruplar Konular'daki sırayla (bültende üyesi yoksa atlanır), sonra grupsuz başlıklar bülten sırasıyla. */
private fun sections(bulletin: Bulletin, topics: Topics?, visible: (Topic) -> List<Story>): List<Section> {
    val groupOf = HashMap<String, com.marjinalbaykus.bulten.data.TopicGroup>()
    topics?.groups.orEmpty().forEach { g -> g.topics.forEach { id -> groupOf.putIfAbsent(id, g) } }
    val out = ArrayList<Section>()
    for (g in topics?.groups.orEmpty()) {
        val members = bulletin.topics.filter { groupOf[it.id]?.id == g.id }
        if (members.isEmpty()) continue
        val items = members.flatMap { m -> visible(m).map { Item(m, it, bulletin.date) } }
            .distinctBy { it.story.sid }
            .sortedByDescending { it.story.importance }
        out += Section("g-${g.id}", g.name, items, "", grouped = true, groupId = g.id)
    }
    for (t in bulletin.topics) {
        if (groupOf[t.id] == null) {
            out += Section("t-${t.id}", t.title, visible(t).map { Item(t, it, bulletin.date) }, t.note, grouped = false)
        }
    }
    return out
}

/** Haberin başlık etiketi: ait olduğu başlık ve varsa diğerleri ("Arda Güler, Real Madrid"). */
fun itemLabel(item: Item, bulletin: Bulletin): String =
    (listOf(item.topic.title) + item.story.also.mapNotNull { id -> bulletin.topics.firstOrNull { it.id == id }?.title })
        .distinct().joinToString(", ")

@Composable
fun Tag(text: String, highlight: Boolean = false, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = if (highlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text, style = MaterialTheme.typography.labelSmall,
            maxLines = 1, overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Column(Modifier.padding(top = 20.dp, bottom = 6.dp)) {
        Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(Modifier.padding(top = 4.dp))
    }
}

@Composable
fun Centered(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { content() }
}

@Composable
fun ErrorView(message: String, title: String = "Bülten yüklenemedi", onRetry: () -> Unit) {
    Centered {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(message, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
            Button(onClick = onRetry) { Text("Tekrar dene") }
        }
    }
}

/** Üç yatay çizgi (menü simgesi). Ek simge kütüphanesi eklememek için elle çiziliyor. */
@Composable
fun MenuIcon() {
    val color = MaterialTheme.colorScheme.onSurface
    Canvas(Modifier.size(22.dp, 16.dp)) {
        val stroke = 2.dp.toPx()
        listOf(0f, size.height / 2, size.height).forEach { y ->
            val yy = y.coerceIn(stroke / 2, size.height - stroke / 2)
            drawLine(color, Offset(0f, yy), Offset(size.width, yy), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}

/** Kutuda gösterilen varlıklar, bu sırayla (S&P 500 en altta). */
private val SHOWN_MARKETS = listOf(
    "USDTRY=X" to "Dolar/TL",
    "EURTRY=X" to "Euro/TL",
    "GRAM" to "Gram altın",
    "XU100.IS" to "BIST 100",
    "^GSPC" to "S&P 500",
)

private fun trNumber(v: Double): String {
    val f = java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("tr-TR"))
    f.maximumFractionDigits = if (v >= 1000) 0 else 2
    f.minimumFractionDigits = if (v >= 1000) 0 else 2
    return f.format(v)
}

private fun trPct(p: Double): String =
    (if (p < 0) "-" else "+") + String.format(java.util.Locale.forLanguageTag("tr-TR"), "%%%.1f", kotlin.math.abs(p))

/**
 * Tarihin sağındaki küçük piyasa tablosu. Yüzde, günlük değişimdir ve değerin sağında parantez içinde
 * küçük yazılır. Eşiği aşan (gerçek alarm) varlığın yüzdesi kalın ve kırmızı/yeşil vurguludur;
 * alarm 1 günlük değilse penceresi de yazılır, örn. (30g -%25,6).
 */
@Composable
private fun MarketMini(bulletin: Bulletin, modifier: Modifier = Modifier) {
    val bySymbol = bulletin.markets.associateBy { it.symbol }
    val alarmBySymbol = bulletin.alarms.associateBy { it.symbol }
    val rows = SHOWN_MARKETS.mapNotNull { (sym, label) -> bySymbol[sym]?.let { Triple(label, it, alarmBySymbol[sym]) } }
    if (rows.isEmpty()) return
    val up = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF7BD88F) else Color(0xFF1B7F3B)
    val down = MaterialTheme.colorScheme.error
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Column(modifier, horizontalAlignment = Alignment.End) {
        rows.forEach { (label, m, alarm) ->
            // Alarm 1 günlük değilse (7 veya 30 gün) o pencerenin yüzdesi gösterilir
            val longAlarm = alarm?.points?.firstOrNull { it.first != "1 günde" }
            val pct = longAlarm?.second ?: m.dayPct
            val pctText = pct?.let { p ->
                val w = longAlarm?.first?.substringBefore(" ")?.let { "${it}g " } ?: ""
                "($w${trPct(p)})"
            } ?: ""
            Row(verticalAlignment = Alignment.Bottom) {
                Text(label, fontSize = 10.sp, lineHeight = 13.sp, color = muted)
                Text(trNumber(m.value), fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End, modifier = Modifier.widthIn(min = 48.dp))
                Text(pctText, fontSize = 9.sp, lineHeight = 13.sp,
                    fontWeight = if (alarm != null) FontWeight.Bold else FontWeight.Normal,
                    color = if ((pct ?: 0.0) >= 0) up else down,
                    textAlign = TextAlign.End, modifier = Modifier.widthIn(min = 52.dp))
            }
        }
    }
}

@Composable
fun BulletinScreen(
    bulletin: Bulletin,
    topics: Topics?,
    offline: Boolean,
    visible: (Topic) -> List<Story>,
    onOpen: (Item) -> Unit,
    padding: PaddingValues,
    listState: LazyListState,
    sortByScore: Boolean,
    unreadOnly: Boolean,
    readKeys: Set<String>,
    reordering: Boolean,
    onReorderStart: () -> Unit,
    onReorderDone: (List<String>) -> Unit,
    onReorderCancel: () -> Unit,
) {
    if (reordering) {
        GroupOrderEditor(
            groups = topics?.groups.orEmpty().map { it.id to it.name },
            onDone = onReorderDone,
            onCancel = onReorderCancel,
            padding = padding,
        )
        return
    }

    val parts = sections(bulletin, topics, visible)
    // Puana göre: tüm haberler tek listede, önem puanı azalan (eşitlikte başlık sırası korunur)
    val byScore = if (sortByScore) {
        bulletin.topics.flatMap { t -> visible(t).map { Item(t, it, bulletin.date) } }
            .distinctBy { it.story.sid }
            .sortedByDescending { it.story.importance }
    } else emptyList()
    val allItems = if (sortByScore) byScore else parts.flatMap { it.items }
    val noUnread = unreadOnly && allItems.none { it.readKey !in readKeys }
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    val base = LocalViewConfiguration.current
    // Grup başlığında uzun basma süresi 1 saniye
    val slowPress = remember(base) {
        object : ViewConfiguration by base {
            override val longPressTimeoutMillis: Long = 1000L
        }
    }
    val haptic = LocalHapticFeedback.current
    val startReorder by rememberUpdatedState {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onReorderStart()
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            val (day, weekday) = trDayParts(bulletin.date)
            Row(Modifier.fillMaxWidth().padding(bottom = 4.dp), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(day, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(weekday, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Hazırlandı ${trPrepared(bulletin.generatedAt, bulletin.date)}" +
                            if (offline) "\nÇevrimdışı kopya" else "",
                        style = MaterialTheme.typography.bodySmall, color = muted,
                    )
                }
                MarketMini(bulletin)
            }
            if (bulletin.scanned > 0) {
                Text(
                    "${trCount(bulletin.scanned)} haber tarandı, ${bulletin.selected} haber seçildi.",
                    style = MaterialTheme.typography.labelSmall, color = muted,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            HorizontalDivider(Modifier.padding(top = 6.dp, bottom = 4.dp))
            if (noUnread) {
                Text("Okunmamış haber yok.", style = MaterialTheme.typography.bodyMedium, color = muted,
                    modifier = Modifier.padding(vertical = 12.dp))
            }
        }
        if (!noUnread) {
            if (sortByScore) {
                val list = if (unreadOnly) byScore.filter { it.readKey !in readKeys } else byScore
                items(list, key = { "p-${it.story.sid}" }) { item ->
                    StoryCard(item, itemLabel(item, bulletin), item.readKey in readKeys) { onOpen(item) }
                }
            } else {
                parts.forEach { part ->
                    item(key = part.key) {
                        val shown = if (unreadOnly) part.items.filter { it.readKey !in readKeys } else part.items
                        Column(Modifier.padding(vertical = 6.dp)) {
                            if (part.groupId != null) {
                                CompositionLocalProvider(LocalViewConfiguration provides slowPress) {
                                    Text(
                                        part.title, style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.fillMaxWidth().pointerInput(Unit) {
                                            detectTapGestures(onLongPress = { startReorder() })
                                        },
                                    )
                                }
                            } else {
                                Text(part.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            }
                            if (part.items.isEmpty()) {
                                Text(
                                    part.note.ifBlank { "Kayda değer bir gelişme yok." },
                                    fontStyle = FontStyle.Italic, color = muted,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            } else if (shown.isEmpty()) {
                                Text("Hepsi okundu.", style = MaterialTheme.typography.bodySmall, color = muted,
                                    modifier = Modifier.padding(top = 2.dp))
                            }
                            shown.forEach { item ->
                                StoryCard(item, if (part.grouped) itemLabel(item, bulletin) else null, item.readKey in readKeys) {
                                    onOpen(item)
                                }
                            }
                        }
                    }
                }
            }
        }
        item { StatsFooter(bulletin) }
    }
}

/** Haber kartı: önem rozeti, (varsa) başlık etiketi ve sadece haber başlığı. Okunmuşsa soluk. */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun StoryCard(item: Item, label: String?, read: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).alpha(if (read) 0.5f else 1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Tag("Önem ${item.story.importance}/10", highlight = item.story.importance >= 8)
                if (label != null) Tag(label, modifier = Modifier.weight(1f, fill = false))
            }
            Spacer(Modifier.height(6.dp))
            Text(item.story.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** "deepseek-flash" -> "DeepSeek Flash", "sonnet" -> "Claude Sonnet". */
private fun modelName(model: String): String {
    val m = model.lowercase()
    return when {
        m.startsWith("deepseek") -> "DeepSeek" + m.removePrefix("deepseek").split('-').filter { it.isNotBlank() }
            .joinToString("") { " " + it.replaceFirstChar { c -> c.uppercase() } }
        "sonnet" in m -> "Claude Sonnet"
        "haiku" in m -> "Claude Haiku"
        "opus" in m -> "Claude Opus"
        else -> model
    }
}

/** Ana sayfanın en altı: hazırlayan model, maliyet, süre ve token. Eski bültenlerde (model yok) gösterilmez. */
@Composable
private fun StatsFooter(b: Bulletin) {
    if (b.model.isBlank()) return
    val tr = java.util.Locale.forLanguageTag("tr-TR")
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val cost = b.costUsd?.let {
        "Maliyet: yaklaşık " + String.format(tr, if (it < 0.01) "%.4f" else "%.2f", it) + " dolar"
    } ?: "Maliyet: Claude aboneliği içinde (ek ücret yok)"
    val parts = ArrayList<String>()
    if (b.seconds > 0) parts += if (b.seconds >= 60) "${b.seconds / 60} dk ${b.seconds % 60} sn" else "${b.seconds} sn"
    if (b.tokens > 0) parts += "${trCount(b.tokens)} token"
    Column(Modifier.fillMaxWidth().padding(top = 20.dp)) {
        HorizontalDivider()
        Text("Hazırlayan model: ${modelName(b.model)}", style = MaterialTheme.typography.labelSmall, color = muted,
            modifier = Modifier.padding(top = 8.dp))
        Text(cost, style = MaterialTheme.typography.labelSmall, color = muted)
        if (parts.isNotEmpty()) {
            Text("Süre: " + parts.joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = muted)
        }
    }
}

/** Sıralama modu: sadece grup adları; satırı tutup sürükleyerek yer değiştirilir. */
@Composable
private fun GroupOrderEditor(
    groups: List<Pair<String, String>>,
    onDone: (List<String>) -> Unit,
    onCancel: () -> Unit,
    padding: PaddingValues,
) {
    BackHandler { onCancel() }
    val order = remember { mutableStateListOf<Pair<String, String>>().also { it.addAll(groups) } }
    val rowPx = with(LocalDensity.current) { 60.dp.toPx() }
    var draggingId by remember { mutableStateOf<String?>(null) }
    var offset by remember { mutableFloatStateOf(0f) }
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    Column(Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Grupları sırala", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Tutup sürükle. Sıra Konular'da da aynı olur.", style = MaterialTheme.typography.bodySmall,
                color = muted, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
            order.toList().forEach { entry ->
                val id = entry.first
                key(id) {
                    val dragging = draggingId == id
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                            .zIndex(if (dragging) 1f else 0f)
                            .graphicsLayer { translationY = if (dragging) offset else 0f }
                            .pointerInput(id) {
                                detectDragGestures(
                                    onDragStart = { draggingId = id; offset = 0f },
                                    onDragEnd = { draggingId = null; offset = 0f },
                                    onDragCancel = { draggingId = null; offset = 0f },
                                    onDrag = { change, amount ->
                                        change.consume()
                                        offset += amount.y
                                        val i = order.indexOfFirst { it.first == id }
                                        if (offset > rowPx / 2 && i in 0 until order.lastIndex) {
                                            order.add(i + 1, order.removeAt(i)); offset -= rowPx
                                        } else if (offset < -rowPx / 2 && i > 0) {
                                            order.add(i - 1, order.removeAt(i)); offset += rowPx
                                        }
                                    },
                                )
                            },
                        color = if (dragging) MaterialTheme.colorScheme.surfaceContainerHighest
                        else MaterialTheme.colorScheme.surface,
                        tonalElevation = if (dragging) 6.dp else 0.dp,
                    ) {
                        Column {
                            Row(Modifier.fillMaxWidth().weight(1f).padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(entry.second, style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                MenuIcon()
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
        Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 12.dp, top = 8.dp,
                    bottom = 8.dp + padding.calculateBottomPadding()),
                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onCancel) { Text("Vazgeç") }
                Button(onClick = { onDone(order.map { it.first }) }) { Text("Bitti") }
            }
        }
    }
}

@Composable
fun DetailScreen(item: Item, padding: PaddingValues) {
    val uri = LocalUriHandler.current
    val context = LocalContext.current
    val s = item.story
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Tag(item.topic.title)
            Tag("Önem ${s.importance}/10", highlight = s.importance >= 8)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { shareStory(context, s) }) { Text("Paylaş") }
        }
        Text(s.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 10.dp))
        val paragraphs = s.detail.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }
        (paragraphs.ifEmpty { listOf(s.headline) }).forEach { p ->
            Text(p, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 10.dp))
        }
        if (s.keyPoints.isNotEmpty()) {
            Text("Öne çıkanlar", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 6.dp))
            s.keyPoints.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp)) }
        }
        if (s.sources.isNotEmpty()) {
            SectionTitle("Kaynaklar (${s.sources.size})")
            s.sources.forEach { src ->
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

/** Haberi başka bir uygulamaya (WhatsApp, Telegram vb.) metin olarak gönderir. */
private fun shareStory(context: Context, s: Story) {
    val link = s.sources.firstOrNull { it.url.isNotBlank() }?.url
    val text = buildString {
        append(s.title)
        append("\n\n")
        append(s.headline)
        if (link != null) append("\n\n").append(link)
    }
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, s.title)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(send, "Haberi paylaş"))
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(index: Index?, onPick: (String) -> Unit, padding: PaddingValues) {
    if (index == null) {
        Centered { CircularProgressIndicator() }
        return
    }
    var picking by remember { mutableStateOf(false) }
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding(),
            bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
        item {
            FilledTonalButton(
                onClick = { picking = true },
                enabled = index.bulletins.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            ) { Text("Tarih seç") }
        }
        items(index.bulletins, key = { it.date }) { b ->
            Row(
                Modifier.fillMaxWidth().clickable { onPick(b.date) }.padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(trDate(b.date, short = true), style = MaterialTheme.typography.bodyLarge)
                Text("${b.count} haber", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            HorizontalDivider()
        }
    }

    if (picking) {
        val utc = java.time.ZoneOffset.UTC
        val days = remember(index) {
            index.bulletins.mapNotNull { runCatching { java.time.LocalDate.parse(it.date) }.getOrNull() }.toSet()
        }
        val newest = days.maxOrNull() ?: java.time.LocalDate.now()
        val oldest = days.minOrNull() ?: newest
        val state = androidx.compose.material3.rememberDatePickerState(
            initialDisplayedMonthMillis = newest.atStartOfDay(utc).toInstant().toEpochMilli(),
            yearRange = oldest.year..newest.year,
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    java.time.Instant.ofEpochMilli(utcTimeMillis).atZone(utc).toLocalDate() in days
            },
        )
        // Bir güne dokununca diyalog kapanır ve o günün bülteni açılır
        LaunchedEffect(state.selectedDateMillis) {
            state.selectedDateMillis?.let {
                picking = false
                onPick(java.time.Instant.ofEpochMilli(it).atZone(utc).toLocalDate().toString())
            }
        }
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { picking = false },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { picking = false }) { Text("Kapat") } },
        ) {
            androidx.compose.material3.DatePicker(
                state = state,
                title = { Text("Hangi günün bülteni?", modifier = Modifier.padding(start = 24.dp, top = 16.dp)) },
                headline = {
                    Text("Bülteni olan bir güne dokun", style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp))
                },
                showModeToggle = false,
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeMode: (ThemeMode) -> Unit,
    token: String,
    onToken: (String) -> Unit,
    notifications: Boolean,
    onNotifications: (Boolean) -> Unit,
    repo: com.marjinalbaykus.bulten.data.Repo,
    existingDates: Set<String>,
    padding: PaddingValues,
) {
    var tokenText by remember { mutableStateOf(token) }
    var saved by remember { mutableStateOf(false) }
    var keepText by remember { mutableStateOf(repo.retentionDays?.toString() ?: "") }
    var keepSaved by remember { mutableStateOf(false) }
    // Elle bülten: önce tarih seçici açılır. Seçilen gün = sadece o günün haberleri;
    // "Bugün" = dün ve bugünün haberleri birlikte (bülten bugünün tarihini alır).
    val tz = java.time.ZoneId.of("Europe/Istanbul")
    val today = remember { java.time.LocalDate.now(tz) }
    var picking by remember { mutableStateOf(false) }
    var runDay by remember { mutableStateOf<java.time.LocalDate?>(null) }   // null = bugün modu
    var confirmStep by remember { mutableIntStateOf(0) }   // 0 kapalı, 1 ilk soru, 2 son onay
    var runMessage by remember { mutableStateOf<String?>(null) }
    var running by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    fun dayLabel(d: java.time.LocalDate): String {
        val (day, weekday) = trDayParts(d.toString())
        return "$day $weekday"
    }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
        SectionTitle("Görünüm")
        Column(Modifier.selectableGroup()) {
            ThemeMode.entries.forEach { m ->
                Row(
                    Modifier.fillMaxWidth()
                        .selectable(selected = m == themeMode, onClick = { onThemeMode(m) }, role = Role.RadioButton)
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = m == themeMode, onClick = null)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(m.label, style = MaterialTheme.typography.bodyLarge)
                        if (m == ThemeMode.SISTEM) {
                            Text("Telefonun açık/koyu ayarını izler", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        SectionTitle("Bildirim")
        Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Yeni bülten bildirimi", style = MaterialTheme.typography.bodyLarge)
                Text("Akşam bülten hazır olunca haber verir (genelde 21:00-21:30 arası).",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = notifications, onCheckedChange = onNotifications)
        }
        SectionTitle("GitHub erişim anahtarı (Araştır ve Konular için)")
        Text(
            "Ara ekranından araştırma başlatmak için gerekir. GitHub > Settings > Developer settings > " +
                "Fine-grained tokens: sadece haber-bulteni deposu, izin olarak Actions: Read and write. " +
                "Ara ekranındaki Araştır ve Konular değişiklikleri bu anahtarla çalışır.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = tokenText,
            onValueChange = { tokenText = it; saved = false },
            placeholder = { Text("github_pat_...") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.keepAboveKeyboard().fillMaxWidth().padding(top = 8.dp),
        )
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            if (saved) Text("Kaydedildi", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
            FilledTonalButton(onClick = { onToken(tokenText); saved = true }) { Text("Kaydet") }
        }
        SectionTitle("Saklama süresi (telefonda)")
        Text(
            "Telefonda kaç günlük bülten saklansın ve aramada kullanılsın. Boş bırakırsan sınırsız saklanır. " +
                "Sitede (GitHub) ayrıca son 2 yıl saklanır.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = keepText,
                onValueChange = { v -> keepText = v.filter { it.isDigit() }.take(5); keepSaved = false },
                placeholder = { Text("Sınırsız") },
                suffix = { Text("gün") },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                modifier = Modifier.keepAboveKeyboard().weight(1f),
            )
            FilledTonalButton(
                onClick = { repo.retentionDays = keepText.toIntOrNull()?.takeIf { it > 0 }; keepSaved = true },
                modifier = Modifier.padding(start = 8.dp),
            ) { Text("Kaydet") }
        }
        if (keepSaved) {
            Text(
                repo.retentionDays?.let { "Kaydedildi: son $it gün saklanır, daha eskiler telefondan silindi." }
                    ?: "Kaydedildi: sınırsız saklanır.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        SectionTitle("Bülteni şimdi hazırla")
        Text(
            "Bülten normalde her akşam 21:00'de kendiliğinden hazırlanır. Kaçan bir gün için buradan elle başlatabilirsin: " +
                "bir tarih seçersen sadece o günün haberleri, \"Bugün\" dersen dün ve bugünün haberleri birlikte taranır. " +
                "DeepSeek ile hazırlanır: ücretlidir (yaklaşık 0,08 dolar) ve 3-5 dakika sürer. Eski günlerde bazı " +
                "kaynaklar (RSS) artık o günü vermediği için haber sayısı biraz azalabilir.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
            FilledTonalButton(onClick = { runMessage = null; picking = true }, enabled = !running) { Text("Hazırla") }
        }
        runMessage?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
        }

        SectionTitle("Hakkında")
        Text("Haber Bülteni ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodyMedium)
        Text("Bülten her akşam 21:00 civarı hazırlanır.", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    if (picking) {
        val oldest = today.minusDays(730)
        val utc = java.time.ZoneOffset.UTC
        val state = androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = null,
            yearRange = oldest.year..today.year,
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val d = java.time.Instant.ofEpochMilli(utcTimeMillis).atZone(utc).toLocalDate()
                    return !d.isAfter(today) && !d.isBefore(oldest)
                }
            },
        )
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { picking = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        runDay = state.selectedDateMillis?.let { java.time.Instant.ofEpochMilli(it).atZone(utc).toLocalDate() }
                        picking = false
                        confirmStep = 1
                    },
                    enabled = state.selectedDateMillis != null,
                ) { Text("Seçilen gün") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { picking = false }) { Text("Vazgeç") }
                    TextButton(onClick = { runDay = null; picking = false; confirmStep = 1 }) { Text("Bugün") }
                }
            },
        ) {
            androidx.compose.material3.DatePicker(
                state = state,
                title = { Text("Hangi günün bülteni?", modifier = Modifier.padding(start = 24.dp, top = 16.dp)) },
                headline = {
                    Text(
                        state.selectedDateMillis?.let {
                            dayLabel(java.time.Instant.ofEpochMilli(it).atZone(utc).toLocalDate())
                        } ?: "Bir gün seç veya Bugün'e bas",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
                    )
                },
                showModeToggle = false,
            )
        }
    }

    if (confirmStep > 0) {
        val day = runDay
        val label = if (day == null) "Bugünün (${dayLabel(today)})" else dayLabel(day)
        val scope2 = if (day == null) "dün ve bugünün haberleri" else "sadece o günün haberleri"
        val exists = (day ?: today).toString() in existingDates
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { confirmStep = 0 },
            title = { Text(if (confirmStep == 1) "Bülten hazırlansın mı?" else "Emin misin?") },
            text = {
                Text(
                    if (confirmStep == 1) {
                        "$label bülteni DeepSeek ile hazırlanacak ($scope2 taranır). Ücretlidir (yaklaşık 0,08 dolar) " +
                            "ve 3-5 dakika sürer." +
                            if (exists) " Bu günün bülteni zaten var, yenisi onun yerine geçer." else ""
                    } else {
                        "$label için çalıştırma şimdi başlatılacak ve DeepSeek bakiyenden düşülecek."
                    },
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (confirmStep == 1) {
                        confirmStep = 2
                    } else {
                        confirmStep = 0
                        running = true
                        scope.launch {
                            runMessage = try {
                                repo.runBulletin(day?.toString())
                                "Başlatıldı. 3-5 dakika sonra Akış'ta aşağı çekip yenile."
                            } catch (e: Exception) {
                                "Başlatılamadı: " + (e.message ?: e.javaClass.simpleName)
                            }
                            running = false
                        }
                    }
                }) { Text(if (confirmStep == 1) "Devam" else "Evet, çalıştır") }
            },
            dismissButton = { TextButton(onClick = { confirmStep = 0 }) { Text("Vazgeç") } },
        )
    }
}
