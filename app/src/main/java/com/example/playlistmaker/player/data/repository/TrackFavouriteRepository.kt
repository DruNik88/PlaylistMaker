package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface TrackFavouriteRepository {
    suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    fun getTrackListInFavourite(): Flow<List<TrackEntity>>
    suspend fun getTrackListIdEntity(): Flow<List<Long>>
}