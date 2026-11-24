package com.marjinalbaykus.bulten

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import com.marjinalbaykus.bulten.data.Bulletin
import com.marjinalbaykus.bulten.data.Index
import com.marjinalbaykus.bulten.data.Repo
import com.marjinalbaykus.bulten.ui.BulletinScreen
import com.marjinalbaykus.bulten.ui.Centered
import com.marjinalbaykus.bulten.ui.DetailScreen
import com.marjinalbaykus.bulten.ui.ErrorView
import com.marjinalbaykus.bulten.ui.HistoryScreen
import com.marjinalbaykus.bulten.ui.Item
import com.marjinalbaykus.bulten.ui.theme.BultenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BultenTheme { App() } }
    }
}

private sealed interface Screen {
    data object Home : Screen
    data object History : Screen
    data class Detail(val item: Item) : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current
    val repo = remember { Repo(context.applicationContext) }

    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    var date by remember { mutableStateOf<String?>(null) }   // null = en son bülten
    var reload by remember { mutableIntStateOf(0) }
    var index by remember { mutableStateOf<Index?>(null) }
    var bulletin by remember { mutableStateOf<Bulletin?>(null) }
    var offline by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    // Liste konumu ekran değişince kaybolmasın (detaydan geri dönünce aynı yerde kalır); bülten değişince başa döner
    val listState = remember(date) { LazyListState() }

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

    BackHandler(enabled = screen != Screen.Home || date != null) {
        when {
            screen != Screen.Home -> screen = Screen.Home
            else -> date = null   // geçmiş bir bültenden en son bültene dön
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (val s = screen) {
                            Screen.Home -> if (date == null) "Günün Özeti" else "Geçmiş bülten"
                            Screen.History -> "Geçmiş bültenler"
                            is Screen.Detail -> s.item.topic.title
                        },
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    if (screen != Screen.Home || date != null) {
                        TextButton(onClick = { if (screen != Screen.Home) screen = Screen.Home else date = null }) {
                            Text("‹ Geri")
                        }
                    }
                },
                actions = {
                    if (screen == Screen.Home) {
                        TextButton(onClick = { screen = Screen.History }) { Text("Geçmiş") }
                        TextButton(onClick = { reload++ }) { Text("Yenile") }
                    }
                },
            )
        },
    ) { padding ->
        when (val s = screen) {
            Screen.Home -> {
                val b = bulletin
                when {
                    loading && b == null -> Centered { CircularProgressIndicator() }
                    error != null && b == null -> ErrorView(error!!) { reload++ }
                    b != null -> BulletinScreen(b, offline, repo::visible, { screen = Screen.Detail(it) }, padding, listState)
                }
            }
            Screen.History -> HistoryScreen(index, { picked ->
                date = picked
                screen = Screen.Home
            }, padding)
            is Screen.Detail -> DetailScreen(s.item, padding)
        }
    }
}
