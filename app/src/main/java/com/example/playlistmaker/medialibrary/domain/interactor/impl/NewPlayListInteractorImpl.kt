package com.example.playlistmaker.medialibrary.domain.interactor.impl

import android.net.Uri
import com.example.playlistmaker.medialibrary.data.NewPlayListRepository
import com.example.playlistmaker.medialibrary.domain.interactor.NewPlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList

class NewPlayListInteractorImpl(
    private val newPlayListRepository: NewPlayListRepository
) : NewPlayListInteractor {
    override suspend fun saveImageUri(uri: Uri): String {
        return newPlayListRepository.saveImageUri(uri)
    }

    override suspend fun saveDataBase(newPlayList: PlayList) {
        newPlayListRepository.saveDataBase(newPlayList)
    }
}