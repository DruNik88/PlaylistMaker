package com.example.playlistmaker.player.ui.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.application.TrackGeneric
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.mapper.TrackGenericInToTrackPlayerDomain
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefPlayerDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.player.service.PlayerService
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.state.ShowPlaylist
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    trackSearch: TrackGeneric,
    private val trackFavouriteInteractor: TrackFavouriteInteractor,
    private val playListPlayerInteractor: PlayListPlayerInteractor
) : ViewModel() {

    private val _showDataLiveData = MutableLiveData<ShowData>()
    fun getShowDataLiveData(): LiveData<ShowData> = _showDataLiveData

    private val _playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = _playStatusLiveData

    private val _showPlaylist = MutableLiveData<ShowPlaylist>()
    fun getPlayListLiveData(): LiveData<ShowPlaylist> = _showPlaylist

    private val _containsTrack = MutableLiveData<Boolean>()
    fun getContainsTrack(): LiveData<Boolean> = _containsTrack

    @SuppressLint("StaticFieldLeak")
    private var playerService: PlayerService? = null

    private val trackPlayerDomain = convertTrackGenericToDomainTrack(trackSearch)
    private lateinit var playListTrackCrossRefDomain: PlayListTrackCrossRefPlayerDomain


    fun setAudioPlayerControl(playerService: PlayerService) {
        this.playerService = playerService

        playerService.getTrack(trackPlayerDomain)

        viewModelScope.launch {
            playerService.playStatus.collect {
                _playStatusLiveData.postValue(it)
            }
        }
        viewModelScope.launch {
            playerService.data.collect {
                _showDataLiveData.postValue(it)
            }
        }
    }

    fun collapsed(){
        playerService?.appCollapsed()
    }

    fun unfold(){
        playerService?.appUnfold()
    }

    fun removeAudioPlayerControl(){
        playerService = null
    }


    fun playbackControl() {
        playerService?.playbackControl()
    }

     fun release() {
        playerService?.release()
    }

    fun addFavourite() {
        viewModelScope.launch {
            if (!trackPlayerDomain.isFavourite) {
                trackPlayerDomain.isFavourite = true
                trackFavouriteInteractor.insertTrackInFavourite(trackPlayerDomain)
                _showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayerDomain))
            } else {
                trackPlayerDomain.isFavourite = false
                trackFavouriteInteractor.deleteTrackInFavourite(trackPlayerDomain)
                _showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayerDomain))
            }
        }
    }

    private fun convertTrackGenericToDomainTrack(trackSearch: TrackGeneric): TrackPlayerDomain {
        return TrackGenericInToTrackPlayerDomain.trackGenericToTrackPlayerDomain(
            trackSearch
        )
    }

    fun addPlayList() {
        viewModelScope.launch {
            playListPlayerInteractor.getPlayList().collect { list ->
                addShowPlaylist(list)
            }
        }
    }

    private fun addShowPlaylist(list: List<PlayListWithTrackPlayer>) {
        if (list.isEmpty()) {
            _showPlaylist.postValue(ShowPlaylist.Empty)
        } else {
            _showPlaylist.postValue(ShowPlaylist.Content(playListData = list))
        }
    }

    fun updatePlayListTableAndAddTrackInTrackTable(playerList: PlayerList) {

        val playList = findPlayListWithTrackById(playerList.id)
        val containsTrack = playList?.let { findTrack(playList) }

        playListTrackCrossRefDomain = PlayListTrackCrossRefPlayerDomain(
            playlistId = playerList.id,
            trackId = trackPlayerDomain.trackId
        )

        if (containsTrack == true) {
            _containsTrack.postValue(true)
        } else {
            _containsTrack.postValue(false)
            viewModelScope.launch {
                playListPlayerInteractor.updatePlayList(playerList, trackPlayerDomain)
                playListPlayerInteractor.addTrackInTrackInPlayListTable(trackPlayerDomain)
                playListPlayerInteractor.addPlayListTrackCrossRef(playListTrackCrossRefDomain)
            }
        }
    }

    private fun findPlayListWithTrackById(id: Long): PlayListWithTrackPlayer? {
        val state = _showPlaylist.value
        return if (state is ShowPlaylist.Content) {
            state.playListData.find { it.playList.id == id }
        } else {
            null
        }
    }

    private fun findTrack(track: PlayListWithTrackPlayer): Boolean {
        val trackList = track.trackList
        return trackList.contains(trackPlayerDomain)

    }
}



