package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BazingaRed,
    secondary = BazingaSurfaceAlt,
    tertiary = BazingaRedDark,
    background = BazingaBackground,
    surface = BazingaSurface,
    surfaceVariant = BazingaSurfaceAlt,
    onPrimary = BazingaTextPrimary,
    onSecondary = BazingaTextPrimary,
    onTertiary = BazingaTextPrimary,
    onBackground = BazingaTextPrimary,
    onSurface = BazingaTextPrimary,
    outline = BazingaBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme || !dynamicColor) {
        DarkColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
