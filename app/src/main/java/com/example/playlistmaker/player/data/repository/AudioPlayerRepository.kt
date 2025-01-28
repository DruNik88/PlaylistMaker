package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.search.domain.model.Track

interface AudioPlayerRepository {
    fun preparePlayer (track: Track, state:(StateAudioPlayer) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayer
}