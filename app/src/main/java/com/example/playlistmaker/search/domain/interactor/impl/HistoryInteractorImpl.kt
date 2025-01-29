package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.model.TrackList
import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class HistoryInteractorImpl(private val trackManager: HistoryRepository): HistoryInteractor {
    override fun addTrackListHistory(track: TrackSearchDomain) {
        trackManager.addTrackListHistory(track)
    }

    override fun getListHistory(): TrackList {
        return trackManager.getListHistory()
    }

    override fun clearHistory() {
        trackManager.clearHistory()
    }

    override fun saveSharedPrefs() {
        trackManager.saveSharedPrefs()
    }
}