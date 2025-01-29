package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

interface TrackListRepository {
    fun searchTrackList (expression: String): Resource<List<TrackSearchDomain>>
}

