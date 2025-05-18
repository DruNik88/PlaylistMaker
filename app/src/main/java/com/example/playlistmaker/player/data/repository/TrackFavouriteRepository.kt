package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface TrackFavouriteRepository {
    fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    fun deleteTrackInFavourite()
    fun getTrackListInFavourite(): Flow<List<TrackEntity>>
}