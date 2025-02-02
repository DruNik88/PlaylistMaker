package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.mapper.TrackSearchDomainInToTrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class AudioPlayerActivityViewModel (
    private val trackSearch: TrackSearchDomain,
    private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private var showDataLiveData = MutableLiveData<ShowData>()
    fun getShowDataLiveData(): LiveData<ShowData> = showDataLiveData

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    init {
        showDataLiveData.postValue(ShowData.Loading)
        val trackPlayer = TrackSearchDomainInToTrackPlayerDomain.trackSearchDomainInToTrackPlayerDomain(trackSearch)
        audioPlayerInteractor.preparePlayer(
            track = trackPlayer,
            playerObserver = object :AudioPlayerInteractor.AudioPlayerObserver{
                override fun onProgress(progress: Long) {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onComplete(){
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                }

                override fun onPause() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }

                override fun onLoad() {
                    showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayer))
                    Log.d("SD_1", "$trackPlayer")
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

    fun pause(){
        audioPlayerInteractor.pausePlayer()
    }

    fun release() {
        audioPlayerInteractor.release()
    }

    companion object {
        fun getViewModelFactory(trackSearch: TrackSearchDomain): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val interactor = Creator.provideGetAudioPlayerManagerInteractor()

                AudioPlayerActivityViewModel(
                    trackSearch = trackSearch,
                    audioPlayerInteractor = interactor
                 )
            }
        }
    }
}


