package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.player.domain.model.TrackPlayer
import com.example.playlistmaker.search.domain.model.Track

interface AudioPlayerInteractor {
    fun preparePlayer (trackPlayer: TrackPlayer, state: (StateAudioPlayer) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayer
}