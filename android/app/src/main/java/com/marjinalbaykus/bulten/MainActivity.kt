package com.marjinalbaykus.bulten

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.marjinalbaykus.bulten.ui.AllNewsScreen
import com.marjinalbaykus.bulten.ui.BulletinScreen
import com.marjinalbaykus.bulten.ui.Centered
import com.marjinalbaykus.bulten.ui.DetailScreen
import com.marjinalbaykus.bulten.ui.ErrorView
import com.marjinalbaykus.bulten.ui.HistoryScreen
import com.marjinalbaykus.bulten.ui.Item
import com.marjinalbaykus.bulten.ui.MenuIcon
import com.marjinalbaykus.bulten.ui.SettingsScreen
import com.marjinalbaykus.bulten.ui.theme.BultenTheme
import com.marjinalbaykus.bulten.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = Repo(applicationContext)
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
    data object AllNews : Screen
    data object History : Screen
    data object Settings : Screen
    data class Detail(val item: Item) : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(repo: Repo, themeMode: ThemeMode, onThemeMode: (ThemeMode) -> Unit) {
    // Ekran yığını: geri tuşu bir önceki ekrana döner (Tüm haberler -> detay -> geri = Tüm haberler)
    var stack by remember { mutableStateOf(listOf<Screen>(Screen.Home)) }
    val screen = stack.last()
    fun push(s: Screen) { stack = stack + s }
    fun pop() { stack = stack.dropLast(1) }

    var date by remember { mutableStateOf<String?>(null) }   // null = en son bülten
    var reload by remember { mutableIntStateOf(0) }
    var index by remember { mutableStateOf<Index?>(null) }
    var bulletin by remember { mutableStateOf<Bulletin?>(null) }
    var offline by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var menuOpen by remember { mutableStateOf(false) }
    // Liste konumları ekran değişince kaybolmasın; bülten değişince başa döner
    val homeState = remember(date) { LazyListState() }
    val allState = remember(date) { LazyListState() }

    LaunchedEffect(date, reload) {
        loading = true
        error = null
        try {
            val idx = repo.index()
            index = idx.value
            val target = date ?: idx.value.bulletins.firstOrNull()?.date ?: error("Henüz bülten yok")
            val b = repo.bulletin(target)
            bulletin = b.value
            offline = idx.offline || b.offline
        } catch (e: Exception) {
            error = e.message ?: e.javaClass.simpleName
        }
        loading = false
    }

    val canGoBack = stack.size > 1 || date != null
    fun back() { if (stack.size > 1) pop() else date = null }   // ana sayfada: geçmiş bültenden en son bültene dön
    BackHandler(enabled = canGoBack) { back() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (screen) {
                            Screen.Home -> if (date == null) "Günün Özeti" else "Geçmiş bülten"
                            Screen.AllNews -> "Tüm haberler"
                            Screen.History -> "Geçmiş bültenler"
                            Screen.Settings -> "Ayarlar"
                            is Screen.Detail -> screen.item.topic.title
                        },
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    if (canGoBack) TextButton(onClick = { back() }) { Text("‹ Geri") }
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuOpen = true }) { MenuIcon() }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(text = { Text("Yenile") }, onClick = { menuOpen = false; reload++ })
                            DropdownMenuItem(text = { Text("Geçmiş bültenler") }, onClick = {
                                menuOpen = false
                                if (screen != Screen.History) push(Screen.History)
                            })
                            DropdownMenuItem(text = { Text("Ayarlar") }, onClick = {
                                menuOpen = false
                                if (screen != Screen.Settings) push(Screen.Settings)
                            })
                        }
                    }
                },
            )
        },
    ) { padding ->
        when (screen) {
            Screen.Home, Screen.AllNews -> {
                val b = bulletin
                when {
                    loading && b == null -> Centered { CircularProgressIndicator() }
                    error != null && b == null -> ErrorView(error!!) { reload++ }
                    b != null && screen == Screen.Home ->
                        BulletinScreen(b, offline, repo::visible, { push(Screen.Detail(it)) }, { push(Screen.AllNews) },
                            padding, homeState)
                    b != null -> AllNewsScreen(b, repo::visible, { push(Screen.Detail(it)) }, padding, allState)
                }
            }
            Screen.History -> HistoryScreen(index, { picked ->
                date = picked
                stack = listOf(Screen.Home)
            }, padding)
            Screen.Settings -> SettingsScreen(themeMode, onThemeMode, padding)
            is Screen.Detail -> DetailScreen(screen.item, padding)
        }
    }
}
