package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain

interface HistoryRepository {
    fun addTrackListHistory(track: TrackSearchDomain)
    fun getListHistory(): TrackSearchListDomain
    fun clearHistory()
    fun saveSharedPrefs()
}