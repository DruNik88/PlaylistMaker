package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.model.PlayerList
import kotlinx.coroutines.flow.Flow

interface PlayListPlayerRepository {
    fun getPlayList(): Flow<List<PlayerList>>
}