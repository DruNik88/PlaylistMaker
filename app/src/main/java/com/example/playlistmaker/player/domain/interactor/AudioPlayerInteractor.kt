package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.player.domain.model.Track

interface AudioPlayerInteractor {
    fun preparePlayer (track: Track, state: (StateAudioPlayer) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayer
}