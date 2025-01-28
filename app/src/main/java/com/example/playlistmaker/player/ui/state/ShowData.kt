package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.TrackPlayer

sealed interface ShowData {
    data class Content(
        val trackModel: TrackPlayer,
    ): ShowData
}