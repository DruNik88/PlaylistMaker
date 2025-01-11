package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.mapper.StateAudioPlayerMapper
import com.example.playlistmaker.data.mapper.TrackOrListMapper
import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.AudioPlayerManager

class AudioPlayerManagerImpl (private val audioPlayerRepository: AudioPlayerRepository): AudioPlayerManager {

    override fun preparePlayer(track: Track, state: (StateAudioPlayer) -> Unit) {
        val trackData = TrackOrListMapper.trackDomainToTrackData(track)
        audioPlayerRepository.preparePlayer(trackData){ playerState ->
            val mapper = StateAudioPlayerMapper.stateAudioPlayer(playerState)
            state(mapper)
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
        val currentPositionAudioPlayerData = audioPlayerRepository.getCurrentPosition()
        val currentPositionAudioPlayer =
            StateAudioPlayerMapper.currentPositionAudioPlayer(
                currentPositionAudioPlayerData = currentPositionAudioPlayerData
            )
        return currentPositionAudioPlayer
    }

}