package com.example.cargrasp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = BackgroundDark,
    primaryContainer = BlueNeon,
    onPrimaryContainer = TextPrimaryDark,
    secondary = IndigoVibrant,
    onSecondary = TextPrimaryDark,
    secondaryContainer = SurfaceCardElevated,
    onSecondaryContainer = TextPrimaryDark,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark,
    error = RedError,
    onError = TextPrimaryDark,
    errorContainer = RedErrorContainer,
    onErrorContainer = RedError
)

private val LightColorScheme = lightColorScheme(
    primary = BlueNeon,
    onPrimary = SurfaceLight,
    primaryContainer = CyanNeon,
    onPrimaryContainer = BackgroundDark,
    secondary = IndigoVibrant,
    onSecondary = SurfaceLight,
    secondaryContainer = SurfaceCardLight,
    onSecondaryContainer = TextPrimaryLight,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceCardLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    error = RedError,
    onError = SurfaceLight,
    errorContainer = RedErrorContainer,
    onErrorContainer = RedError
)

@Composable
fun CarGraspTheme(
    darkTheme: Boolean = true, // Default to sleek dark automotive theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
