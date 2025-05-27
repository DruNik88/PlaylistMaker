package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getTrackListInFavourite(): Flow<List<TrackFavourite>>
}