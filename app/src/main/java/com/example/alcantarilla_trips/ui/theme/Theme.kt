package com.example.alcantarilla_trips.ui.theme

import androidx.compose.ui.graphics.Color
import android.app.Activity
import android.os.Build
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
    primary = CheeseYellow,
    onPrimary = SewerDark,
    secondary = TailPink,
    onSecondary = SewerDark,
    tertiary = RatGreyLight,
    background = SewerDark,
    surface = SewerSurface,
    onBackground = CheeseYellowDim,
    onSurface = RatGreyLight,
)

private val LightColorScheme = lightColorScheme(
    primary = CheeseYellowDark,
    onPrimary = Color.White,
    secondary = TailPinkDark,
    onSecondary = Color.White,
    tertiary = RatGrey,
    background = SewerLight,
    surface = Color.White,
    onBackground = RatGreyDark,
    onSurface = RatGreyDark,
)

@Composable
fun Alcantarilla_TripsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}