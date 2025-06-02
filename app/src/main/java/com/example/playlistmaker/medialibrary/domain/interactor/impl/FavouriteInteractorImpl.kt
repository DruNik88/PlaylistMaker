package com.example.playlistmaker.medialibrary.domain.interactor.impl

import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.domain.interactor.FavouriteInteractor
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(
    private val favouriteRepository: FavouriteRepository
) : FavouriteInteractor {
    override suspend fun getTrackListInFavourite(): Flow<List<TrackFavourite>> {
        return favouriteRepository.getTrackListInFavourite()
    }
}