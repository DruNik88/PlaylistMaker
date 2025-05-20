package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun addTrackListHistory(track: TrackSearchDomain)
    suspend fun getListHistory(): Flow<List<TrackSearchDomain>>
    fun clearHistory()
    fun saveSharedPrefs()
}