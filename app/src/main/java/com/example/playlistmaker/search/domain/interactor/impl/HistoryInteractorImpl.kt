package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val trackManager: HistoryRepository): HistoryInteractor {
    override fun addTrackListHistory(track: TrackSearchDomain) {
        trackManager.addTrackListHistory(track)
    }

    override suspend fun getListHistory(): Flow<TrackSearchListDomain> {
        return trackManager.getListHistory()
    }

    override fun clearHistory() {
        trackManager.clearHistory()
    }

    override fun saveSharedPrefs() {
        trackManager.saveSharedPrefs()
    }
}