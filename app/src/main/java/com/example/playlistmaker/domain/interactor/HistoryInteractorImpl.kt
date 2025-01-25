package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList
import com.example.playlistmaker.domain.repository.HistoryRepository

class HistoryInteractorImpl(private val trackManager: HistoryRepository): HistoryInteractor {
    override fun addTrackListHistory(track: Track) {
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