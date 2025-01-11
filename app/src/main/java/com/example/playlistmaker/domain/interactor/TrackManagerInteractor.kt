package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList

interface TrackManagerInteractor {
    fun addTrackListHistory(track: Track)
    fun getListHistory(): TrackList
    fun clearHistory()
    fun saveSharedPrefs()
}