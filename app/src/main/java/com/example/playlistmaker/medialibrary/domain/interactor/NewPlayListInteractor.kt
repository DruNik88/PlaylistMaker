package com.example.playlistmaker.medialibrary.domain.interactor

import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.model.PlayList

interface NewPlayListInteractor {
    suspend fun saveImageUri(uri: Uri): String
    suspend fun saveDataBase(newPlayList: PlayList)
    suspend fun updateDataBase(playList: PlayList)

}