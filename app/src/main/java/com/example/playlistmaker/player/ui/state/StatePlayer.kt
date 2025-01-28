package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.StateAudioPlayer

sealed interface StatePlayer {
    data class Prepared(
        val currentState: StateAudioPlayer
    ): StatePlayer
    data class Playing(
        val currentState: StateAudioPlayer
    ): StatePlayer
    data class Pause(
        val currentState: StateAudioPlayer
    ): StatePlayer

}