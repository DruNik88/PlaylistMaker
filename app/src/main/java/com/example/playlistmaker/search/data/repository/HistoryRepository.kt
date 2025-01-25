package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

interface HistoryRepository {
    fun addTrackListHistory(track: Track)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}