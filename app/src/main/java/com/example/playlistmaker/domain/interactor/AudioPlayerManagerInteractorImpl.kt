package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.AudioPlayerManager

class AudioPlayerManagerInteractorImpl (private val audioPlayerManager: AudioPlayerManager, ) :
    AudioPlayerManagerInteractor {
    override fun preparePlayer(track: Track, state: (StateAudioPlayer) -> Unit){
        return audioPlayerManager.preparePlayer(track) {
                playerState -> state(playerState)
        }
    }

    override fun startPlayer() {
        audioPlayerManager.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerManager.pausePlayer()
    }
    override fun release(){
        audioPlayerManager.release()
    }

    override fun getCurrentPosition(): CurrentPositionAudioPlayer {
        return audioPlayerManager.getCurrentPosition()
    }
}