package com.example.playlistmaker.medialibrary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.NewPlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.launch

class NewPlayListRedactViewModel(
    private val playList: PlayList,
    private val newPlayListInteractor: NewPlayListInteractor
) : NewPlayListViewModel(
    newPlayListInteractor = newPlayListInteractor
) {
    override val title = MutableLiveData<String?>()
    override val description = MutableLiveData<String?>()
    override val imageLocalStoragePath = MutableLiveData<String?>()

    private val _playListLiveDta = MutableLiveData<PlayList>()
    fun getPlayListLiveData(): LiveData<PlayList> = _playListLiveDta

    init {
        _playListLiveDta.postValue(playList)
    }

    fun updatePlayList() {
        val data = _playListLiveDta.value
        val updatingPlayList = data?.let {
            data.copy(
                title = title.value,
                description = description.value,
                imageLocalStoragePath = imageLocalStoragePath.value
            )
        }

        viewModelScope.launch {
            updatingPlayList?.let {
                newPlayListInteractor.updateDataBase(it)
            }
        }
    }
}