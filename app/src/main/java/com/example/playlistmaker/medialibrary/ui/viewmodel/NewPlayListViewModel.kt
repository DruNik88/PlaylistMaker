package com.example.playlistmaker.medialibrary.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.NewPlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.launch

class NewPlayListViewModel(
    private val newPlayListInteractor: NewPlayListInteractor
) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String?>()
    val imageUri = MutableLiveData<Uri?>()

    private val playList: LiveData<PlayList> = MediatorLiveData<PlayList>().apply {
        fun update() {
            value = PlayList(
                title = title.value,
                description = description.value,
                imageInnerUri = imageUri.value
            )
        }
        addSource(title) { update() }
        addSource(description) { update() }
        addSource(imageUri) { update() }
    }

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun updateDescription(newDescription: String?) {
        description.value = newDescription
    }

    fun updateImageUri(newImageUri: Uri) {
        viewModelScope.launch {
            newPlayListInteractor.saveImageUri(newImageUri)
        }
        imageUri.value = newImageUri
    }

    fun saveDataBase() {
        viewModelScope.launch {
            val data = PlayList(
                title = title.value,
                description = description.value,
                imageInnerUri = imageUri.value
            )
            data.let{newPlayListInteractor.saveDataBase(data)}
        }
    }

}