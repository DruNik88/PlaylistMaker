package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.model.TrackPlayerDomain


interface AudioPlayerInteractor {
    fun preparePlayer(track: TrackPlayerDomain, playerObserver: AudioPlayerObserver)

    fun pausePlayer()
    fun release()
    suspend fun playbackControl()

    interface AudioPlayerObserver {
        fun onProgress(progress: Long)
        fun onComplete()
        fun onPause()
        fun onPlay()
        fun onLoad()
    }
}