package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.data.model.StateAudioPlayerData
import com.example.playlistmaker.data.model.TrackData

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
    }

    private var playerState = StateAudioPlayerData(state = STATE_DEFAULT)

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(track: TrackData, state: (StateAudioPlayerData) -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.state = STATE_PREPARED
            state(playerState)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.state = STATE_PREPARED
            state(playerState)
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

    override fun getCurrentPosition(): CurrentPositionAudioPlayerData {
        return CurrentPositionAudioPlayerData(currentPosition = mediaPlayer.currentPosition)
    }


}