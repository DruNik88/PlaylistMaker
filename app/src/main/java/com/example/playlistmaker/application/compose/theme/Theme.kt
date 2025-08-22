package com.example.playlistmaker.application.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkTheme = darkColorScheme(
    surface = Yp_Black,
    onSurface = Yp_White,
    primary = Yp_Black,
    onPrimary = Yp_White,
    primaryContainer = Yp_White,
    onPrimaryContainer = Yp_White,
    secondary = Yp_White,
    onSecondary = Yp_Black,
//    secondaryContainer = Yp_Blue_Light,
)

private val LightTheme = lightColorScheme(
    surface = Yp_White,
    onSurface = Yp_Black,
    primary = Yp_Gray,
    onPrimary = Yp_Gray_Light,
    primaryContainer = Yp_Gray_Light,
    secondary = Yp_Gray_Light,
    onSecondary = Yp_Gray,
//    secondaryContainer = Yp_Blue_Light,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkTheme
    } else {
        LightTheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}