package com.example.playlistmaker.medialibrary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInfoInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.launch

class PlayListInfoViewModel(
    val playList: PlayList,
    private val playListInfoInteractor: PlayListInfoInteractor
): ViewModel() {

    private val _showLiveData = MutableLiveData<PlayListWithTrackDetail>()
    fun getShowLiveData(): LiveData<PlayListWithTrackDetail> = _showLiveData

    init {
        _showLiveData.postValue(PlayListWithTrackDetail.Loading)
        val playListId = playList.id
        viewModelScope.launch {
            playListInfoInteractor.getPlayListWithTrackDetail(playListId).collect{ playlist->
                addShowLiveData(playlist)
            }
        }
    }

    private fun addShowLiveData(playList: PlayListWithTrackMediaLibrary){
        if (playList.trackList.isEmpty()){
            _showLiveData.postValue(PlayListWithTrackDetail.Empty)
        } else {
            _showLiveData.postValue(PlayListWithTrackDetail.Content(playListData = playList))
        }
    }


}