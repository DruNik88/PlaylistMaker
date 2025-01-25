package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track

interface TrackListRepository {
    fun searchTrackList (expression: String): Resource<List<Track>>
}

