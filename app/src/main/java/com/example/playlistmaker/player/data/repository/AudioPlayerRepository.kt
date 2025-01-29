package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

interface AudioPlayerRepository {

    fun preparePlayer (track: TrackPlayerDomain, playerObserver: AudioPlayerInteractor.AudioPlayerObserver)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
}