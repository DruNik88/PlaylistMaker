package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track

interface AudioPlayerRepository {
    fun preparePlayer (track: Track, state:(StateAudioPlayer) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayer
}