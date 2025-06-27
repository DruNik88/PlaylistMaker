package com.example.playlistmaker.medialibrary.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.NewPlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.launch

open class NewPlayListViewModel(
    private val newPlayListInteractor: NewPlayListInteractor
) : ViewModel() {

    open val title = MutableLiveData<String?>()
    open val description = MutableLiveData<String?>()
    open val imageLocalStoragePath = MutableLiveData<String?>()

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun updateDescription(newDescription: String?) {
        description.value = newDescription
    }

    fun updateImageUri(newImageUri: Uri) {
        viewModelScope.launch {
            val imageName = newPlayListInteractor.saveImageUri(newImageUri)
            this@NewPlayListViewModel.imageLocalStoragePath.value = imageName
        }
    }

    fun saveDataBase() {
        viewModelScope.launch {
            val data = PlayList(
                title = title.value,
                description = description.value,
                imageLocalStoragePath = imageLocalStoragePath.value
            )
            data.let { newPlayListInteractor.saveDataBase(data) }
        }
    }

}