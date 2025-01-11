package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.TrackData
import com.example.playlistmaker.data.model.TrackListHistory

interface TrackRepository {
    fun addTrackListHistory(track: TrackData)
    fun getListHistory(): TrackListHistory
    fun clearHistory()
    fun saveSharedPrefs()
}
