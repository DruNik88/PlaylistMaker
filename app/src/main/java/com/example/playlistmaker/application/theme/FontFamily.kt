package com.example.playlistmaker.application.theme


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.playlistmaker.R

val YsDisplay = FontFamily(
    Font(R.font.ys_display_bold, FontWeight.Bold),
    Font(R.font.ys_display_heavy, FontWeight.ExtraBold),
    Font(R.font.ys_display_light, FontWeight.Light),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_regular, FontWeight.W400),
    Font(R.font.ys_display_thin, FontWeight.Thin)
)

val YsText = FontFamily(
        Font(R.font.ys_text_bold, FontWeight.Bold),
    Font(R.font.ys_text_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.ys_text_light, FontWeight.Light),
    Font(R.font.ys_text_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.ys_text_medium, FontWeight.Medium),
    Font(R.font.ys_text_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.ys_text_regular, FontWeight.W400),
    Font(R.font.ys_text_regular_italic, FontWeight.W400, FontStyle.Italic),
)