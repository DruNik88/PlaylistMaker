package com.example.playlistmaker.medialibrary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.ui.state.PlayListData
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    private var _playlist = MutableLiveData<PlayListData>()
    val getPlaylist: LiveData<PlayListData> = _playlist

    init {
        viewModelScope.launch {
            playListInteractor.getPlayList().collect { playList ->
                showPlayList(playList)
            }
        }
    }

    private fun showPlayList(playlist: List<PlayList>) {
        if (playlist.isEmpty()) {
            _playlist.postValue(PlayListData.Empty)
        } else {
            _playlist.postValue(PlayListData.Content(playListData = playlist))
        }
    }

}
