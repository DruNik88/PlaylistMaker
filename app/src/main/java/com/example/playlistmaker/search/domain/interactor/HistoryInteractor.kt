package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain

interface HistoryInteractor {
    fun addTrackListHistory(track: TrackSearchDomain)
    fun getListHistory(): TrackSearchListDomain
    fun clearHistory()
    fun saveSharedPrefs()
}