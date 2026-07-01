package com.snappy.samsung.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Sky400,
    secondary = Indigo500,
    background = androidx.compose.ui.graphics.Color.Black,
    surface = androidx.compose.ui.graphics.Color.Black,
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    onSecondary = Slate50,
    onBackground = Slate50,
    onSurface = Slate50
)

private val LightColorScheme = lightColorScheme(
    primary = Sky500,
    secondary = Indigo600,
    background = Slate50,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = Slate900,
    onSurface = Slate900
)

@Composable
fun SnappyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
