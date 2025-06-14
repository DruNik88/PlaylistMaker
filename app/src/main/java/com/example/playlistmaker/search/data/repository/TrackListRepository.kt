package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    fun searchTrackList(expression: String): Flow<Resource<List<TrackSearchDomain>>>
}

