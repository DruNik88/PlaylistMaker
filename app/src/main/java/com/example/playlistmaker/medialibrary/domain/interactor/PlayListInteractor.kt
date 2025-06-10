package com.example.playlistmaker.medialibrary.domain.interactor

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun getPlayList(): Flow<List<PlayList>>
}