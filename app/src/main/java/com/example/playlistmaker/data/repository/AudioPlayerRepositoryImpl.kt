package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.data.mapper.StateAudioPlayerMapper
import com.example.playlistmaker.data.mapper.TrackOrListMapper
import com.example.playlistmaker.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.data.model.StateAudioPlayerData
import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.domain.model.StateAudioPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
    }

    private var playerState = StateAudioPlayerData(state = STATE_DEFAULT)

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(track: Track, state: (StateAudioPlayer) -> Unit) {
        val trackData = TrackOrListMapper.trackDomainToTrackData(track)
        mediaPlayer.setDataSource(trackData.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.state = STATE_PREPARED
            val mapper = StateAudioPlayerMapper.stateAudioPlayer(playerState)
            state(mapper)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.state = STATE_PREPARED
            val mapper = StateAudioPlayerMapper.stateAudioPlayer(playerState)
            state(mapper)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): CurrentPositionAudioPlayer {
        val currentPositionAudioPlayerData = CurrentPositionAudioPlayerData(currentPosition = mediaPlayer.currentPosition)
        val currentPositionAudioPlayer =
            StateAudioPlayerMapper.currentPositionAudioPlayer(
                currentPositionAudioPlayerData = currentPositionAudioPlayerData
            )
        return currentPositionAudioPlayer
    }


}