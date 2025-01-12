package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList

interface HistoryRepository {
    fun addTrackListHistory(track: Track)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}