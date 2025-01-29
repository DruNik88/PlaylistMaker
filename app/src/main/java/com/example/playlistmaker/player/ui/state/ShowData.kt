package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

sealed interface ShowData {
    data class Content(
        val trackModel: TrackPlayerDomain,
    ): ShowData
}