package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

class AudioPlayerInteractorImpl(
    private val audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {
    
    override fun preparePlayer(
        track: TrackPlayerDomain,
        playerObserver: AudioPlayerInteractor.AudioPlayerObserver
    ) {
        audioPlayerRepository.preparePlayer(
            track = track,
            playerObserver = playerObserver
        )
    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun release() {
        audioPlayerRepository.release()
    }

    override fun playbackControl() {
        audioPlayerRepository.playbackControl()
    }
}