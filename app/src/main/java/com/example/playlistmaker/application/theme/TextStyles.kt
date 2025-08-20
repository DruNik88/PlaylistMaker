package com.example.playlistmaker.application.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.application.theme.YsDisplay

object AppTextStyles {
    val BottomAppBarStyle = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

    val TopAppBarStyle = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )

    val SettingsStyle = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )

    val TrackItemTrackNameStyle = TextStyle (
        fontFamily = YsDisplay,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )

    val TrackItemArtistNameAndTrackTime = TextStyle (
        fontFamily = YsDisplay,
        fontWeight = FontWeight.W400,
        fontSize = 11.sp,
        lineHeight = 11.sp,
        letterSpacing = 0.sp
    )

    val SearchStyle = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

    val YouFindStyle = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp
    )
}