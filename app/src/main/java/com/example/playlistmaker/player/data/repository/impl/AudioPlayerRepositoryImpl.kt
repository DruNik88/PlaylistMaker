package com.example.playlistmaker.player.data.repository.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.mapper.StateAudioPlayerMapper
import com.example.playlistmaker.search.data.mapper.TrackOrListMapper
import com.example.playlistmaker.settings.data.model.CurrentPositionAudioPlayerData
import com.example.playlistmaker.player.data.model.StateAudioPlayerData
import com.example.playlistmaker.domain.model.CurrentPositionAudioPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
    }

    private var playerState = StateAudioPlayerData(state = STATE_DEFAULT)

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(track: Track, state: (StateAudioPlayer) -> Unit) {
        val trackData = TrackOrListMapper.trackDomaInToTrackData(track)
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