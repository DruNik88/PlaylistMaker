package com.example.playlistmaker.medialibrary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInfoInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.launch

class PlayListInfoViewModel(
    val playList: PlayList,
    private val playListInfoInteractor: PlayListInfoInteractor
) : ViewModel() {

    private val _showLiveData = MutableLiveData<PlayListWithTrackDetail>()
    fun getShowLiveData(): LiveData<PlayListWithTrackDetail> = _showLiveData
    private var playListId = 0L

    init {
        _showLiveData.postValue(PlayListWithTrackDetail.Loading)
        playListId = playList.id
        viewModelScope.launch {
            playListInfoInteractor.getPlayListWithTrackDetail(playListId).collect { playlist ->
                addShowLiveData(playlist)
            }
        }
    }

    private fun addShowLiveData(playList: PlayListWithTrackMediaLibrary) {
        if (playList.trackList.isEmpty()) {
            _showLiveData.postValue(PlayListWithTrackDetail.Empty(playList = playList.playList))
        } else {
            _showLiveData.postValue(
                PlayListWithTrackDetail.Content(
                    playListData = playList
                )
            )
        }
    }

    fun deleteTrackFromPlaylist(track: TrackMediaLibraryDomain) {

        val crossRef = PlayListTrackCrossRefMediaLibraryDomain(
            trackId = track.trackId,
            playlistId = playListId
        )

        viewModelScope.launch {
            playListInfoInteractor.deleteTrackFromPlaylist(crossRef, track)
        }
    }

    fun sharePlayList() {

        val playlistWithTrack =
            if (_showLiveData.value is PlayListWithTrackDetail.Content)
                (_showLiveData.value as PlayListWithTrackDetail.Content).playListData
            else
                null


        val infoPlaylist = playlistWithTrack?.let {
            buildString {
                appendLine(playlistWithTrack.playList.title)
                appendLine(playlistWithTrack.playList.description)
                appendLine(trackEndings(playlistWithTrack.playList.count))
                appendLine(playlistWithTrack.trackList.mapIndexed { index, trackList ->
                    "${index + 1}. ${trackList.artistName} - ${trackList.trackName} (${trackList.trackTimeMillis})"
                })
            }
        } ?: ""
        playListInfoInteractor.sharePlayList(infoPlaylist)
    }

    fun deletePlayList() {
        if (_showLiveData.value is PlayListWithTrackDetail.Content) {
            val actualPlayList =
                (_showLiveData.value as PlayListWithTrackDetail.Content).playListData.playList
            viewModelScope.launch {
                playListInfoInteractor.deletePlayList(actualPlayList)
            }
        } else {
            val actualPlayList =
                (_showLiveData.value as PlayListWithTrackDetail.Empty).playList
            viewModelScope.launch {
                playListInfoInteractor.deletePlayList(actualPlayList)
            }
        }
    }
}