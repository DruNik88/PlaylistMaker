package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    suspend fun searchTrackList(expression: String): Flow<Resource<List<TrackSearchDomain>>>
}