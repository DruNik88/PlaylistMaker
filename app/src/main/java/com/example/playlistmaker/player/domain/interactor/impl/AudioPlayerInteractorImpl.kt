package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.search.domain.model.Track

class AudioPlayerInteractorImpl (private val audioPlayerRepository: AudioPlayerRepository, ) :
    AudioPlayerInteractor {
    override fun preparePlayer(track: Track, state: (StateAudioPlayer) -> Unit){
        audioPlayerRepository.preparePlayer(track) {
                playerState -> state(playerState)
        }
    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }
    override fun release(){
        audioPlayerRepository.release()
    }

    override fun getCurrentPosition(): CurrentPositionAudioPlayer {
        return audioPlayerRepository.getCurrentPosition()
    }
}