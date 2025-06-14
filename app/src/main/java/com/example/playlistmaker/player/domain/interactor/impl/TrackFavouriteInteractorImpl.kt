package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain


class TrackFavouriteInteractorImpl(
    private val trackFavouriteRepository: TrackFavouriteRepository
) : TrackFavouriteInteractor {
    override suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        trackFavouriteRepository.insertTrackInFavourite(trackPlayerDomain)
    }

    override suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        trackFavouriteRepository.deleteTrackInFavourite(trackPlayerDomain)
    }
}