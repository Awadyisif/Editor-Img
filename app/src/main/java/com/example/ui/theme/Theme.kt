package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = StudioAmber,
    onPrimary = StudioCharcoal,
    primaryContainer = StudioAmberContainer,
    onPrimaryContainer = StudioAmberLight,
    secondary = StudioAmberLight,
    onSecondary = StudioCharcoal,
    background = StudioCharcoal,
    onBackground = StudioCream,
    surface = StudioDarkSurface,
    onSurface = StudioCream,
    surfaceVariant = StudioCardDark,
    onSurfaceVariant = Color(0xFFA5A9B4),
    outline = StudioBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = StudioAmber,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFDE8DA),
    onPrimaryContainer = StudioCharcoal,
    secondary = Color(0xFF944B1D),
    onSecondary = Color.White,
    background = StudioCream,
    onBackground = StudioTextDark,
    surface = StudioPureWhite,
    onSurface = StudioTextDark,
    surfaceVariant = StudioIvory,
    onSurfaceVariant = StudioTextSecondary,
    outline = Color(0xFFE5E0D8)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
