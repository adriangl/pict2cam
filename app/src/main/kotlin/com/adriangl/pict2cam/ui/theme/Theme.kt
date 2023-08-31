package com.adriangl.pict2cam.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryVariantDark,
    secondary = secondaryColor,
    secondaryVariant = secondaryVariantDark,
    background = Color.Black,
    surface = Color.Black,
    error = Color.Red,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariantLight,
    secondary = secondaryColor,
    secondaryVariant = secondaryVariantLight,
    background = Color.White,
    surface = Color.White,
    error = Color.Red,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

/**
 * Main theme of the application.
 */
@Composable
fun Pict2CamTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                  content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Pict2CamTypography,
        shapes = Shapes,
        content = content
    )
}
