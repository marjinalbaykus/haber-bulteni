package com.marjinalbaykus.bulten.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.BuildConfig
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Index
import com.marjinalbaykus.bulten.data.Story
import com.marjinalbaykus.bulten.data.Topic
import com.marjinalbaykus.bulten.ui.theme.ThemeMode

/** Bir haber ve ait olduğu başlık. */
data class Item(val topic: Topic, val story: Story)

@Composable
fun Tag(text: String, highlight: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = if (highlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text, style = MaterialTheme.typography.labelSmall,
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
fun ErrorView(message: String, onRetry: () -> Unit) {
    Centered {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bülten yüklenemedi", style = MaterialTheme.typography.titleMedium)
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
 * Küçük piyasa kutusu: iki sütunda kurlar ve endeksler. Eşiği aşan (gerçek alarm) varlığın hücresinin altında
 * küçük kırmızı bir etiket çıkar; ayrı bir alarm listesi yok, böylece aynı isim iki kez yazılmaz.
 */
@Composable
private fun MarketCard(bulletin: Bulletin) {
    val bySymbol = bulletin.markets.associateBy { it.symbol }
    val alarmBySymbol = bulletin.alarms.associateBy { it.symbol }
    val cells = SHOWN_MARKETS.mapNotNull { (sym, label) -> bySymbol[sym]?.let { Triple(label, it, alarmBySymbol[sym]) } }
    if (cells.isEmpty()) return
    val up = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF7BD88F) else Color(0xFF1B7F3B)
    val down = MaterialTheme.colorScheme.error
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text("Piyasalar", style = MaterialTheme.typography.labelMedium, color = muted)
            cells.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { (label, m, alarm) ->
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(label, style = MaterialTheme.typography.labelSmall, color = muted,
                                    maxLines = 1, modifier = Modifier.weight(1f))
                                Text(trNumber(m.value), style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold)
                                Text(m.dayPct?.let(::trPct) ?: "", style = MaterialTheme.typography.labelSmall,
                                    color = if ((m.dayPct ?: 0.0) >= 0) up else down, textAlign = TextAlign.End,
                                    modifier = Modifier.widthIn(min = 44.dp))
                            }
                            alarm?.points?.forEach { (window, pct) ->
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    modifier = Modifier.padding(top = 2.dp),
                                ) {
                                    Text(if (window == "1 günde") "Günlük alarm" else "$window ${trPct(pct)}", style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp))
                                }
                            }
                        }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun BulletinScreen(
    bulletin: Bulletin,
    offline: Boolean,
    visible: (Topic) -> List<Story>,
    onOpen: (Item) -> Unit,
    padding: PaddingValues,
    listState: LazyListState,
) {
    val perTopic = bulletin.topics.map { it to visible(it) }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(trDate(bulletin.date), style = MaterialTheme.typography.titleMedium)
            Text(
                "Hazırlandı: ${trTime(bulletin.generatedAt)}" + if (offline) "  (çevrimdışı kopya)" else "",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item { MarketCard(bulletin) }
        item { SectionTitle("Kısa özet") }
        perTopic.forEach { (topic, stories) ->
            item(key = "ozet-${topic.id}") {
                Column(Modifier.padding(vertical = 6.dp)) {
                    Text(topic.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    if (stories.isEmpty()) {
                        Text(
                            topic.note.ifBlank { "Kayda değer bir gelişme yok." },
                            fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    stories.forEach { s ->
                        Row(
                            Modifier.fillMaxWidth().clickable { onOpen(Item(topic, s)) }.padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text("${s.importance}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(20.dp))
                            Text(s.headline, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllNewsScreen(
    bulletin: Bulletin,
    visible: (Topic) -> List<Story>,
    onOpen: (Item) -> Unit,
    padding: PaddingValues,
    listState: LazyListState,
) {
    // Tüm haberler önem puanına göre (eşitlikte başlık sırası korunur)
    val all = bulletin.topics.flatMap { t -> visible(t).map { Item(t, it) } }.sortedByDescending { it.story.importance }
    if (all.isEmpty()) {
        Centered { Text("Bu bültende haber yok.") }
        return
    }
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
            bottom = padding.calculateBottomPadding() + 32.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Row(Modifier.fillMaxWidth().padding(bottom = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(trDate(bulletin.date), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Toplam ${all.size} haber", style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            }
        }
        items(all, key = { "kart-${it.story.sid}" }) { item ->
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable { onOpen(item) }) {
                Column(Modifier.padding(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Tag("Önem ${item.story.importance}/10", highlight = item.story.importance >= 8)
                        Tag(item.topic.title)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(item.story.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(item.story.headline, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3)
                }
            }
        }
    }
}

@Composable
fun DetailScreen(item: Item, padding: PaddingValues) {
    val uri = LocalUriHandler.current
    val s = item.story
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Tag(item.topic.title)
            Tag("Önem ${s.importance}/10", highlight = s.importance >= 8)
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

@Composable
fun HistoryScreen(index: Index?, onPick: (String) -> Unit, padding: PaddingValues) {
    if (index == null) {
        Centered { CircularProgressIndicator() }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding(),
            bottom = padding.calculateBottomPadding() + 32.dp),
    ) {
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
}

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeMode: (ThemeMode) -> Unit,
    token: String,
    onToken: (String) -> Unit,
    padding: PaddingValues,
) {
    var tokenText by remember { mutableStateOf(token) }
    var saved by remember { mutableStateOf(false) }
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
        SectionTitle("Araştırma (GitHub erişim anahtarı)")
        Text(
            "Ara ekranından araştırma başlatmak için gerekir. GitHub > Settings > Developer settings > " +
                "Fine-grained tokens: sadece haber-bulteni deposu, izin olarak Actions: Read and write.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = tokenText,
            onValueChange = { tokenText = it; saved = false },
            placeholder = { Text("github_pat_...") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        )
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            if (saved) Text("Kaydedildi", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
            FilledTonalButton(onClick = { onToken(tokenText); saved = true }) { Text("Kaydet") }
        }
        SectionTitle("Hakkında")
        Text("Haber Bülteni ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodyMedium)
        Text("Bülten her sabah 08:30 civarı hazırlanır.", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
