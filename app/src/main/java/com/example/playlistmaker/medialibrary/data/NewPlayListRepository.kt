package com.example.playlistmaker.medialibrary.data

import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.model.PlayList

interface NewPlayListRepository {
    suspend fun saveImageUri(uri: Uri): String
    suspend fun saveDataBase(newPlayList: PlayList)
    suspend fun updateDataBase(playList: PlayList)

}