package com.example.playlistmaker.medialibrary.domain.model

data class PlayListWithTrackMediaLibrary(
    val playList: PlayList,
    val trackList: List<TrackMediaLibraryDomain>
)
