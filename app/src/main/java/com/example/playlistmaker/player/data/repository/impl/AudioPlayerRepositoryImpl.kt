package com.example.playlistmaker.player.data.repository.impl

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.data.mapper.TrackPlayerDomainInToTrackPlayerData
import com.example.playlistmaker.player.data.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
        private const val AVAILABLE_TIME = 30000L
    }

    private lateinit var innerPlayerObserver: AudioPlayerInteractor.AudioPlayerObserver

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())


    private fun createUpdateTimerAudioPlayer(): Runnable {
        return object : Runnable {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun run() {
                if (playerState == STATE_PLAYING && mediaPlayer.isPlaying) {
                    val reverseTimer = AVAILABLE_TIME - mediaPlayer.currentPosition
                    innerPlayerObserver.onProgress(progress = reverseTimer)
                    mainThreadHandler.postDelayed(this,DELAY)
                }
            }

        }
    }

    override fun preparePlayer(
        track: TrackPlayerDomain,
        playerObserver: AudioPlayerInteractor.AudioPlayerObserver) {

        val trackPlayerData= TrackPlayerDomainInToTrackPlayerData.trackPlayerDomainInToTrackPlayerData(track)

        innerPlayerObserver = playerObserver

        mediaPlayer.setDataSource(trackPlayerData.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            playerObserver.onComplete()
            playerObserver.onLoad()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playerObserver.onPause()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun startPlayer() {
        mediaPlayer.start()

        playerState = STATE_PLAYING

        innerPlayerObserver.onPlay()

        mainThreadHandler.post(createUpdateTimerAudioPlayer())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun pausePlayer() {
            mediaPlayer.pause()

            playerState = STATE_PAUSED

        innerPlayerObserver.onPause()

        mainThreadHandler.removeCallbacks(createUpdateTimerAudioPlayer())
    }

    override fun release() {
        mediaPlayer.release()
    }


    override fun playbackControl() {
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