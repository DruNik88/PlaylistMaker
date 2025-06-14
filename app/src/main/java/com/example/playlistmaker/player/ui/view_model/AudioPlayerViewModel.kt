package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.mapper.TrackSearchDomainInToTrackPlayerDomain
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.player.ui.model.PlayStatus
import com.example.playlistmaker.player.ui.state.ShowData
import com.example.playlistmaker.player.ui.state.ShowPlaylist
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    trackSearch: TrackSearchDomain,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val trackFavouriteInteractor: TrackFavouriteInteractor,
    private val playListPlayerInteractor: PlayListPlayerInteractor
) : ViewModel() {

    private val _showDataLiveData = MutableLiveData<ShowData>()
    fun getShowDataLiveData(): LiveData<ShowData> = _showDataLiveData

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    private val _showPlaylist = MutableLiveData<ShowPlaylist>()
    fun getPlayListLiveData(): LiveData<ShowPlaylist> = _showPlaylist

    private val _containsTrack = MutableLiveData<Boolean>()
    fun getContainsTrack(): LiveData<Boolean> = _containsTrack

    private val trackPlayerDomain = convertSearchTrackToDomainTrack(trackSearch)
    lateinit var playListTrackCrossRefDomain: PlayListTrackCrossRefDomain

    init {
        _showDataLiveData.postValue(ShowData.Loading)
        val trackPlayer = trackPlayerDomain

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
                    _showDataLiveData.postValue(ShowData.Content(trackModel = trackPlayer))
                }
            }
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0L, isPlaying = false)
    }

    fun playbackControl() {
        viewModelScope.launch {
            audioPlayerInteractor.playbackControl()
        }
    }

    fun pause() {
        audioPlayerInteractor.pausePlayer()
    }

    fun release() {
        audioPlayerInteractor.release()
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

    private fun convertSearchTrackToDomainTrack(trackSearch: TrackSearchDomain): TrackPlayerDomain {
        return TrackSearchDomainInToTrackPlayerDomain.trackSearchDomainInToTrackPlayerDomain(
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

        playListTrackCrossRefDomain = PlayListTrackCrossRefDomain(
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



