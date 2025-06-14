package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    fun getPlayList(): Flow<List<PlayList>>
}