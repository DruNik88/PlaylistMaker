package com.example.playlistmaker.medialibrary.domain.interactor


import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {
    suspend fun getTrackListInFavourite(): Flow<List<TrackFavourite>>
}