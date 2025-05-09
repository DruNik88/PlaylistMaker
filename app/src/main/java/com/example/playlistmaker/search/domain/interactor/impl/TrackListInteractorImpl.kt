package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import kotlinx.coroutines.flow.Flow

class TrackListInteractorImpl(private val trackListRepository: TrackListRepository) :
    TrackListInteractor {
    override suspend fun searchTrackList(expression: String): Flow<Resource<List<TrackSearchDomain>>> {
        return trackListRepository.searchTrackList(expression)
    }
}