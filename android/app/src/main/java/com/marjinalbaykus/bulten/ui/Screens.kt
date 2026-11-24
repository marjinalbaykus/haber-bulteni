package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Index
import com.marjinalbaykus.bulten.data.Story
import com.marjinalbaykus.bulten.data.Topic

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
    // En alttaki liste: tüm haberler önem puanına göre (eşitlikte başlık sırası korunur)
    val all = perTopic.flatMap { (t, ss) -> ss.map { Item(t, it) } }.sortedByDescending { it.story.importance }

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
        if (bulletin.alarms.isNotEmpty()) {
            item {
                Card(Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Piyasa alarmı", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        bulletin.alarms.forEach { a -> Text("• ${a.name}: ${a.lines.joinToString(", ")}") }
                    }
                }
            }
        }
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
        item { SectionTitle("Tüm haberler (puana göre)") }
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
