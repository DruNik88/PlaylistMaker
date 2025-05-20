package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.model.TrackFavourite

class TrackFavouriteInteractorImpl (
    private val trackFavouriteRepository: TrackFavouriteRepository
): TrackFavouriteInteractor {
    override suspend fun insertTrackInFavourite(trackPlayerDomain: TrackFavourite) {
       trackFavouriteRepository.insertTrackInFavourite(trackPlayerDomain)
    }

    override suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackFavourite) {
        trackFavouriteRepository.deleteTrackInFavourite(trackPlayerDomain)
    }
}