package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList
import com.example.playlistmaker.domain.repository.TrackManager

class TrackManagerInteractorImpl(private val trackManager: TrackManager): TrackManagerInteractor {
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