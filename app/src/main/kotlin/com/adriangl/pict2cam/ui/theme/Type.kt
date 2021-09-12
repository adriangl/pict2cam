package com.adriangl.pict2cam.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.adriangl.pict2cam.R

// Set of Material typography styles to start with
val openSansNormalFontFamily = FontFamily(
    Font(R.font.opensans_light_normal, FontWeight.Light),
    Font(R.font.opensans_regular_normal, FontWeight.Medium),
    Font(R.font.opensans_semibold_normal, FontWeight.SemiBold),
    Font(R.font.opensans_bold_normal, FontWeight.Bold),
    Font(R.font.opensans_extrabold_normal, FontWeight.ExtraBold)
)

val openSansItalicFontFamily = FontFamily(
    Font(R.font.opensans_light_italic, FontWeight.Light),
    Font(R.font.opensans_regular_italic, FontWeight.Medium),
    Font(R.font.opensans_semibold_italic, FontWeight.SemiBold),
    Font(R.font.opensans_bold_italic, FontWeight.Bold),
    Font(R.font.opensans_extrabold_italic, FontWeight.ExtraBold)
)

val Pict2CamTypography = Typography(
    defaultFontFamily = openSansNormalFontFamily,
)
