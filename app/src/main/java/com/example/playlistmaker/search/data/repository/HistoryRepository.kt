package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun addTrackListHistory(track: TrackSearchDomain)
    fun getListHistory(): Flow<List<TrackSearchDomain>>
    fun clearHistory()
    fun saveSharedPrefs()
}