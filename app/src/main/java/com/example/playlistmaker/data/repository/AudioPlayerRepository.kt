package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.data.model.StateAudioPlayerData
import com.example.playlistmaker.data.model.TrackData

interface AudioPlayerRepository {
    fun preparePlayer (track: TrackData, state:(StateAudioPlayerData) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getCurrentPosition(): CurrentPositionAudioPlayerData
}