package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackList

interface HistoryInteractor {
    fun addTrackListHistory(track: TrackSearchDomain)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}