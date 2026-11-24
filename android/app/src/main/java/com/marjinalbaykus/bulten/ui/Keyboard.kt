package com.marjinalbaykus.bulten.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay

/**
 * Yazı alanına dokununca klavye açılırken alanı görünür yere kaydırır (klavyenin arkasında kalmasın).
 * Ekran içeriği MainActivity'de imePadding ile klavyenin üstüne daraltılıyor; bu da odaklanan alanı o alana getirir.
 */
fun Modifier.keepAboveKeyboard(): Modifier = composed {
    val requester = remember { BringIntoViewRequester() }
    var focused by remember { mutableStateOf(false) }
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    LaunchedEffect(focused, imeBottom) {
        if (focused && imeBottom > 0) {
            delay(80)   // klavye animasyonu ve yeniden yerleşim bitsin (her yeni yükseklikte yeniden başlar)
            requester.bringIntoView()
        }
    }
    bringIntoViewRequester(requester).onFocusEvent { focused = it.isFocused }
}
