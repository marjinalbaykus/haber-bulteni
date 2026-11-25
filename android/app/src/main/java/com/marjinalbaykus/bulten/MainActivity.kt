package com.marjinalbaykus.bulten

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Index
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.ui.AppIcons
import com.marjinalbaykus.bulten.ui.BulletinScreen
import com.marjinalbaykus.bulten.ui.withTitles
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import com.marjinalbaykus.bulten.ui.TopicsDraft
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.marjinalbaykus.bulten.ui.Centered
import com.marjinalbaykus.bulten.ui.DetailScreen
import com.marjinalbaykus.bulten.ui.ErrorView
import com.marjinalbaykus.bulten.ui.HistoryScreen
import com.marjinalbaykus.bulten.ui.Item
import com.marjinalbaykus.bulten.ui.ResearchScreen
import com.marjinalbaykus.bulten.ui.SearchScreen
import com.marjinalbaykus.bulten.ui.SettingsScreen
import com.marjinalbaykus.bulten.ui.TopicsScreen
import com.marjinalbaykus.bulten.notify.NewsJob
import com.marjinalbaykus.bulten.ui.theme.BultenTheme
import com.marjinalbaykus.bulten.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = Repo(applicationContext)
        runCatching { repo.pruneCache() }   // telefondaki saklama süresi
        runCatching { NewsJob.schedule(applicationContext) }   // bildirim kurulamazsa uygulama yine açılsın
        // Android 13+ bildirim izni (bir kez sorulur; reddedilirse Ayarlar'dan açılabilir)
        if (Build.VERSION.SDK_INT >= 33 && repo.notificationsOn &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
                .launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            var mode by remember { mutableStateOf(repo.themeMode) }
            val dark = when (mode) {
                ThemeMode.SISTEM -> isSystemInDarkTheme()
                ThemeMode.ACIK -> false
                ThemeMode.KOYU -> true
            }
            // Durum çubuğu simgeleri uygulamanın temasına uysun (telefonun temasına değil)
            LaunchedEffect(dark) {
                val style = if (dark) SystemBarStyle.dark(Color.TRANSPARENT)
                else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
            }
            BultenTheme(mode) {
                App(repo, mode) { mode = it; repo.themeMode = it }
            }
        }
    }
}

private sealed interface Screen {
    data object Home : Screen
    data object History : Screen
    data object Settings : Screen
    data object Search : Screen
    data object Topics : Screen
    data class ResearchDetail(val id: String) : Screen
    data class Detail(val item: Item) : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(repo: Repo, themeMode: ThemeMode, onThemeMode: (ThemeMode) -> Unit) {
    // Ekran yığını: geri tuşu bir önceki ekrana döner (Ara -> Geçmiş -> geri = Ara)
    var stack by remember { mutableStateOf(listOf<Screen>(Screen.Home)) }
    val screen = stack.last()
    fun push(s: Screen) { stack = stack + s }
    fun pop() { stack = stack.dropLast(1) }
    var readKeys by remember { mutableStateOf(repo.readKeys()) }
    // Haber açılınca okundu olarak işaretlenir (soluk görünür)
    fun openItem(item: Item) {
        repo.markRead(item.readKey)
        readKeys = repo.readKeys()
        push(Screen.Detail(item))
    }

    var date by remember { mutableStateOf<String?>(null) }   // null = en son bülten
    var reload by remember { mutableIntStateOf(0) }
    var index by remember { mutableStateOf<Index?>(null) }
    var bulletin by remember { mutableStateOf<Bulletin?>(null) }
    var offline by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var sortMenu by remember { mutableStateOf(false) }
    var sortByScore by remember { mutableStateOf(repo.sortByScore) }
    var unreadOnly by remember { mutableStateOf(repo.unreadOnly) }
    var reordering by remember { mutableStateOf(false) }
    var collapsed by remember { mutableStateOf(repo.collapsedGroups) }
    var searchQuery by remember { mutableStateOf("") }
    var notifications by remember { mutableStateOf(repo.notificationsOn) }
    var topics by remember { mutableStateOf(repo.topicsCache) }   // güncel başlık adları ve gruplar
    val scope = rememberCoroutineScope()
    val topicsDraft = remember { TopicsDraft() }   // Konular'daki kaydedilmemiş değişiklikler
    // Liste konumları ekran değişince kaybolmasın; bülten değişince başa döner
    val homeState = remember(date) { LazyListState() }

    LaunchedEffect(date, reload) {
        loading = true
        error = null
        try {
            val idx = repo.index()
            index = idx.value
            val target = date ?: idx.value.bulletins.firstOrNull()?.date ?: error("Henüz bülten yok")
            val b = repo.bulletin(target)
            bulletin = b.value
            if (date == null) repo.lastSeenDate = b.value.date   // bu bülten için bildirim gelmesin
            offline = idx.offline || b.offline
        } catch (e: Exception) {
            error = e.message ?: e.javaClass.simpleName
        }
        loading = false
        // Konu adları/gruplar: alınamazsa bülten kendi başlıklarıyla gösterilir
        runCatching { repo.topics().value }.onSuccess { topics = it; repo.topicsCache = it }
    }

    // Alt çubuktaki sekmeler; yığının ilk elemanı hangi sekmede olunduğunu gösterir
    val tab = stack.first()
    val showBackButton = stack.size > 1 || (tab == Screen.Home && date != null)
    fun back() {
        when {
            stack.size > 1 -> pop()
            tab != Screen.Home -> stack = listOf(Screen.Home)
            else -> date = null   // ana sayfada: geçmiş bültenden en son bültene dön
        }
    }
    BackHandler(enabled = stack.size > 1 || tab != Screen.Home || date != null) { back() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (screen) {
                            Screen.Home -> if (date == null) "Akış" else "Geçmiş bülten"
                            Screen.History -> "Geçmiş bültenler"
                            Screen.Settings -> "Ayarlar"
                            Screen.Search -> "Ara"
                            Screen.Topics -> "Konular"
                            is Screen.ResearchDetail -> "Araştırma"
                            is Screen.Detail -> screen.item.topic.title
                        },
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    if (showBackButton) TextButton(onClick = { back() }) { Text("‹ Geri") }
                },
                actions = {
                    // Sağ üstte küçük sıralama ve yenile simgeleri (sadece Akış'ta, sıralama modunda gizli)
                    if (screen == Screen.Home && !reordering) {
                        Box {
                            IconButton(onClick = { sortMenu = true }) {
                                Icon(AppIcons.Sort, contentDescription = "Sırala", modifier = Modifier.size(20.dp))
                            }
                            DropdownMenu(expanded = sortMenu, onDismissRequest = { sortMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Gruplara göre") },
                                    leadingIcon = { RadioButton(selected = !sortByScore, onClick = null) },
                                    onClick = { sortMenu = false; sortByScore = false; repo.sortByScore = false },
                                )
                                DropdownMenuItem(
                                    text = { Text("Puana göre") },
                                    leadingIcon = { RadioButton(selected = sortByScore, onClick = null) },
                                    onClick = { sortMenu = false; sortByScore = true; repo.sortByScore = true },
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Sadece okunmamışlar") },
                                    leadingIcon = { Checkbox(checked = unreadOnly, onCheckedChange = null) },
                                    onClick = { sortMenu = false; unreadOnly = !unreadOnly; repo.unreadOnly = unreadOnly },
                                )
                                DropdownMenuItem(
                                    text = { Text("Grupları sırala") },
                                    enabled = topics?.groups?.isNotEmpty() == true,
                                    onClick = { sortMenu = false; sortByScore = false; repo.sortByScore = false; reordering = true },
                                )
                            }
                        }
                        IconButton(onClick = { reload++ }) {
                            Icon(AppIcons.Refresh, contentDescription = "Yenile", modifier = Modifier.size(20.dp))
                        }
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                listOf(
                    Triple(Screen.Home, "Akış", AppIcons.Home),
                    Triple(Screen.Search, "Ara", AppIcons.Search),
                    Triple(Screen.Topics, "Konular", AppIcons.Topics),
                    Triple(Screen.Settings, "Ayarlar", AppIcons.Settings),
                ).forEach { (target, label, icon) ->
                    NavigationBarItem(
                        selected = tab == target,
                        onClick = {
                            // Zaten bu sekmedeyken tekrar basınca listenin başına döner
                            if (stack == listOf(target)) {
                                if (target == Screen.Home) scope.launch { homeState.animateScrollToItem(0) }
                            } else {
                                stack = listOf(target)
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { scaffoldPadding ->
      // Klavye açılınca içerik klavyenin üstüne sığacak şekilde daralır ve alt çubuk payı kalkar;
      // böylece odaklanan yazı alanı klavyenin üstüne kaydırılabilir (ui/Keyboard.kt)
      val imeOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
      val padding = if (imeOpen) PaddingValues(top = scaffoldPadding.calculateTopPadding()) else scaffoldPadding
      Box(Modifier.fillMaxSize().imePadding()) {
        when (screen) {
            Screen.Home -> {
                val b = bulletin
                when {
                    loading && b == null -> Centered { CircularProgressIndicator() }
                    error != null && b == null -> ErrorView(error!!) { reload++ }
                    b != null -> {
                        // Aşağı çekince bülten yeniden indirilir (uygulama arkada açık kaldıysa yeni bülten gelsin)
                        PullToRefreshBox(
                            isRefreshing = loading,
                            onRefresh = { reload++ },
                            modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding()),
                        ) {
                            val inner = PaddingValues(bottom = padding.calculateBottomPadding())
                            val shown = remember(b, topics) { b.withTitles(topics) }
                            BulletinScreen(
                                shown, topics, offline, repo::visible, ::openItem, inner, homeState,
                                sortByScore = sortByScore,
                                unreadOnly = unreadOnly,
                                readKeys = readKeys,
                                reordering = reordering,
                                onReorderStart = { reordering = true },
                                onReorderDone = { ids ->
                                    reordering = false
                                    val t = topics?.withOrder(ids)
                                    repo.groupOrder = ids
                                    if (t != null) {
                                        topics = t
                                        repo.topicsCache = t
                                        // Sıra sunucuya da kaydedilir (anahtar yoksa sadece telefonda kalır)
                                        if (repo.githubToken.isNotBlank()) {
                                            scope.launch { runCatching { repo.saveGroups(t.groups) } }
                                        }
                                    }
                                },
                                onReorderCancel = { reordering = false },
                                collapsed = collapsed,
                                onToggleCollapse = { key ->
                                    collapsed = if (key in collapsed) collapsed - key else collapsed + key
                                    repo.collapsedGroups = collapsed
                                },
                            )
                        }
                    }
                }
            }
            Screen.History -> HistoryScreen(index, { picked ->
                date = picked
                stack = listOf(Screen.Home)
            }, padding)
            Screen.Settings -> SettingsScreen(themeMode, onThemeMode, repo.githubToken, { repo.githubToken = it },
                notifications, { notifications = it; repo.notificationsOn = it }, repo,
                index?.bulletins?.map { it.date }?.toSet().orEmpty(), padding)
            Screen.Search -> SearchScreen(repo, searchQuery, { searchQuery = it }, { push(Screen.ResearchDetail(it)) },
                ::openItem, { stack = listOf(Screen.Settings) }, { push(Screen.History) }, padding)
            Screen.Topics -> TopicsScreen(repo, topicsDraft, { stack = listOf(Screen.Settings) }, { topics = it }, padding)
            is Screen.ResearchDetail -> ResearchScreen(repo, screen.id, padding)
            is Screen.Detail -> DetailScreen(screen.item, padding)
        }
      }
    }
}
