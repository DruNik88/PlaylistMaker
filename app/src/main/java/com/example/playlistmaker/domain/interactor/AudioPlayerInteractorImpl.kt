package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

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