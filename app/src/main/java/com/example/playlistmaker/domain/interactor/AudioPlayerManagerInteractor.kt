package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track

interface AudioPlayerManagerInteractor {
    fun preparePlayer (track: Track, state: (StateAudioPlayer) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayer
}