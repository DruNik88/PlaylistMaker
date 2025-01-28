package com.example.playlistmaker.player.ui.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.mapper.TrackSearchInToTrackPlayer
import com.example.playlistmaker.player.domain.model.StateAudioPlayer
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.state.StatePlayer
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivityViewModel (
    private val trackSearch: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var showDataLiveData = MutableLiveData<ShowData>()
    fun getShowDataLiveData(): LiveData<ShowData> = showDataLiveData

    private val playStatusLiveData = MutableLiveData<StatePlayer>()
    fun getPlayStatusLiveData(): LiveData<StatePlayer> = playStatusLiveData

//    init {
//        tracksInteractor.loadTrackData(
//            trackId = trackId,
//            onComplete = { trackModel ->
//                // 1
//                screenStateLiveData.postValue(
//                    TrackScreenState.Content(trackModel)
//                )
//            }
//        )
//    }


    init{
        showData()
    }
    init {
        val trackPlayer = TrackSearchInToTrackPlayer.trackSearchInToTrackPlayer(trackSearch)
        audioPlayerInteractor.preparePlayer(trackPlayer) { state -> playStatusLiveData.postValue(StatePlayer.Prepared(currentState = state)) }
    }

    private fun showData(){
        val trackPlayer = TrackSearchInToTrackPlayer.trackSearchInToTrackPlayer(trackSearch)
        showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayer))

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()

        var state = getCurrentState()
        state = StatePlayer.Playing(currentState = StateAudioPlayer(state = STATE_PLAYING))

        mainThreadHandler?.post(createUpdateTimerAudioPlayer())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()

        var state = getCurrentState()
        state = StatePlayer.Playing(currentState = StateAudioPlayer(state = STATE_PAUSED))

        mainThreadHandler?.removeCallbacks(createUpdateTimerAudioPlayer())
    }

    fun playbackControl() {
        val playerState = getCurrentState()
        when (playerState) {
            is StatePlayer.Playing -> {
                pausePlayer()
            }

            is StatePlayer.Prepared,
            is StatePlayer.Pause -> {
            startPlayer()
        }

    }
    }

    private fun createUpdateTimerAudioPlayer(): Runnable {
        return object : Runnable {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val reverseTimer =
                        AVAILABLE_TIME - audioPlayerManager.getCurrentPosition().currentPosition

                    if (reverseTimer >= 0) {
                        playbackProgress.text =
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(reverseTimer)
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }

                } else {
                    mainThreadHandler?.removeCallbacks(this)
                    playbackControl.setImageDrawable(
                        getResources().getDrawable(
                            R.drawable.ic_playback_control,
                            null
                        )
                    )
                }
            }
        }
    }

    private fun getCurrentState(): StatePlayer{
        return playStatusLiveData.value ?: StatePlayer.Prepared(currentState = StateAudioPlayer(state = STATE_DEFAULT))
    }



//    fun play() {
//        trackPlayer.play(
//            trackId = trackId,
//            // 1
//            statusObserver = object : AudioPlayerRep.StatusObserver {
//                override fun onProgress(progress: Float) {
//                    // 2
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
//                }
//
//                override fun onStop() {
//                    // 3
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
//                }
//
//                override fun onPlay() {
//                    // 4
//                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
//                }
//            },
//        )
//    }
//
//    // 5
//    fun pause() {
//        trackPlayer.pause(trackId)
//    }
//
//    // 6
//    override fun onCleared() {
//        trackPlayer.release(trackId)
//    }
//
//    private fun getCurrentPlayStatus(): PlayStatus {
//        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
//    }

    companion object {
        fun getViewModelFactory(trackSearch: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val interactor = Creator.provideGetAudioPlayerManagerInteractor()

                AudioPlayerActivityViewModel(
                    trackSearch = trackSearch,
                    audioPlayerInteractor = interactor
                 )
            }
        }

        private const val RADIUS_IMAGE = 8.0F
        private const val KEY_TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
        private const val AVAILABLE_TIME = 30000L
    }
}
//
//companion object {
//    fun getViewModelFactory(trackSearch: Track?): ViewModelProvider.Factory = viewModelFactory {
//        initializer {
//            val myApp = this[APPLICATION_KEY] as App
//            val interactor = Creator.provideGetAudioPlayerManagerInteractor()
//            val trackPlayer = myApp.provideTrackPlayer()
//
//            AudioPlayerActivityViewModel(
//                trackSearch,
//                interactor,
//                trackPlayer,
//            )
//        }
//    }
//}


//class AudioPlayerActivityViewModel (
//    private val trackSearch: Track?,
//    private val tracksInteractor: TracksInteractor,
//    private val trackPlayer: AudioPlayerRep,



