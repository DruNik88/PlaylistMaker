package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

interface TrackFavouriteRepository {
    suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
}