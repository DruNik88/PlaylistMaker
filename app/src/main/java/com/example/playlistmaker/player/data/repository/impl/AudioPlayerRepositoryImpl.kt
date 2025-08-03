package com.example.playlistmaker.player.data.repository.impl

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.mapper.TrackPlayerDomainInToTrackPlayerData
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.delay

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
        private const val AVAILABLE_TIME = 30000L
    }

    private lateinit var innerPlayerObserver: AudioPlayerInteractor.AudioPlayerObserver

    private var playerState = STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = null

    private suspend fun createUpdateTimerAudioPlayer() {
        mediaPlayer?.let {
            while (playerState == STATE_PLAYING && it.isPlaying) {
                val reverseTimer = AVAILABLE_TIME - it.currentPosition
                delay(DELAY)
                innerPlayerObserver.onProgress(progress = reverseTimer)
            }
        }
    }
    override fun preparePlayer(
        track: TrackPlayerDomain,
        playerObserver: AudioPlayerInteractor.AudioPlayerObserver
    ) {
        val trackPlayerData =
            TrackPlayerDomainInToTrackPlayerData.trackPlayerDomainInToTrackPlayerData(track)

        innerPlayerObserver = playerObserver

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        mediaPlayer?.setDataSource(trackPlayerData.previewUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            playerState = STATE_PREPARED
            playerObserver.onLoad()
        }
        mediaPlayer?.setOnCompletionListener {
            playerState = STATE_PREPARED
            playerObserver.onComplete()
            playerObserver.onPause()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    suspend fun startPlayer() {
        mediaPlayer?.start()

        playerState = STATE_PLAYING

        innerPlayerObserver.onPlay()

        createUpdateTimerAudioPlayer()
    }

    override fun pausePlayer() {

        try {
            mediaPlayer?.let {
                if (playerState == STATE_PLAYING && it.isPlaying) {
                    mediaPlayer?.pause()
                    playerState = STATE_PAUSED
                    innerPlayerObserver.onPause()
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun release() {

        playerState = STATE_DEFAULT
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override suspend fun playbackControl() {

        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()

            }
        }
    }
}
