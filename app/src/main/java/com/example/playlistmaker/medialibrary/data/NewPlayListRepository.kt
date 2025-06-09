package com.example.playlistmaker.medialibrary.data

import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.model.PlayList

interface NewPlayListRepository {
    suspend fun saveImageUri(uri: Uri)
    suspend fun saveDataBase(newPlayList: PlayList)
}