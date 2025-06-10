package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.model.PlayerList
import kotlinx.coroutines.flow.Flow

interface PlayListPlayerInteractor {
    suspend fun getPlayList(): Flow<List<PlayerList>>
}