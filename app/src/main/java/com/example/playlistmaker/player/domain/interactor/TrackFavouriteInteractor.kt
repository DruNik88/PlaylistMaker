package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface TrackFavouriteInteractor {
    fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    fun deleteTrackInFavourite()
    fun getTrackListInFavourite(): Flow<List<TrackEntity>>
}