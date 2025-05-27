package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.model.TrackPlayerDomain

interface TrackFavouriteInteractor {
    suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
    suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain)
}