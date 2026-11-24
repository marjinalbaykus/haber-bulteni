package com.marjinalbaykus.bulten.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    secondaryContainer = LightPrimaryContainer,
    onSecondaryContainer = LightOnPrimaryContainer,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outlineVariant = LightOutlineVariant,
    surfaceContainerHighest = LightCard,
    surfaceContainer = LightCard,
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    secondaryContainer = DarkPrimaryContainer,
    onSecondaryContainer = DarkOnPrimaryContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outlineVariant = DarkOutlineVariant,
    surfaceContainerHighest = DarkCard,
    surfaceContainer = DarkCard,
)

/** Kullanıcının seçtiği görünüm. SISTEM telefonun ayarını izler. */
enum class ThemeMode(val label: String) {
    SISTEM("Sistem"),
    ACIK("Açık"),
    KOYU("Koyu"),
}

@Composable
fun BultenTheme(mode: ThemeMode = ThemeMode.SISTEM, content: @Composable () -> Unit) {
    val dark = when (mode) {
        ThemeMode.SISTEM -> isSystemInDarkTheme()
        ThemeMode.ACIK -> false
        ThemeMode.KOYU -> true
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
