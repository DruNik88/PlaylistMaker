package com.example.playlistmaker.medialibrary.ui.state

import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite

sealed class FavouriteData {
    data class Content(
        val favouriteTrackList: List<TrackFavourite>
    ) : FavouriteData()

    data object Empty : FavouriteData()
}


