package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CommandDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = CyberNavyCard,
    onPrimaryContainer = NeonCyan,
    secondary = WarningAmber,
    onSecondary = Color.Black,
    secondaryContainer = CyberNavyElevated,
    onSecondaryContainer = WarningAmber,
    tertiary = WaterBlue,
    onTertiary = Color.White,
    background = CyberNavyDark,
    onBackground = TextPrimary,
    surface = CyberNavy,
    onSurface = TextPrimary,
    surfaceVariant = CyberNavyCard,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
    error = CriticalRed,
    onError = Color.White
)

@Composable
fun JammuRakshakTheme(
    content: @Composable () -> Unit
) {
    // Always use the dedicated high-contrast cyber command center dark palette
    MaterialTheme(
        colorScheme = CommandDarkColorScheme,
        typography = Typography,
        content = content
    )
}
