package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.mapper.TrackSearchDomainInToTrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class AudioPlayerViewModel(
    trackSearch: TrackSearchDomain,
    private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private var showDataLiveData = MutableLiveData<ShowData>()
    fun getShowDataLiveData(): LiveData<ShowData> = showDataLiveData

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    init {
        showDataLiveData.postValue(ShowData.Loading)
        val trackPlayer =
            TrackSearchDomainInToTrackPlayerDomain.trackSearchDomainInToTrackPlayerDomain(
                trackSearch
            )
        audioPlayerInteractor.preparePlayer(
            track = trackPlayer,
            playerObserver = object : AudioPlayerInteractor.AudioPlayerObserver {
                override fun onProgress(progress: Long) {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onComplete() {
                    playStatusLiveData.value =
                        getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                }

                override fun onPause() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }

                override fun onLoad() {
                    showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayer))
                }
            }
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0L, isPlaying = false)
    }

    fun playbackControl() {
        audioPlayerInteractor.playbackControl()
    }

    fun pause() {
        audioPlayerInteractor.pausePlayer()
    }

    fun release() {
        audioPlayerInteractor.release()
    }
}


