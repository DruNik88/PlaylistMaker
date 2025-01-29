package com.example.playlistmaker.player.ui.model

import java.text.SimpleDateFormat
import java.util.Locale

data class PlayStatus(
    val progress: Long,
    val isPlaying: Boolean
) {

    fun formatProgress(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(progress)

}