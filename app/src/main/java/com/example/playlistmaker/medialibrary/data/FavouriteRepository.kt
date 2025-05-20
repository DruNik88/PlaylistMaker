package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.player.domain.model.TrackFavourite
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getTrackListInFavourite(): Flow<List<TrackFavourite>>
}