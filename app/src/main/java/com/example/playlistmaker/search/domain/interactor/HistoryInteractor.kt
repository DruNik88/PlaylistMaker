package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

interface HistoryInteractor {
    fun addTrackListHistory(track: Track)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}