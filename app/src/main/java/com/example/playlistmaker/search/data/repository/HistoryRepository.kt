package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackList

interface HistoryRepository {
    fun addTrackListHistory(track: TrackSearchDomain)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}