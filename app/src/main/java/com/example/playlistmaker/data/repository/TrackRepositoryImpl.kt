package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.data.model.TrackData
import com.example.playlistmaker.data.model.TrackListHistory
import com.google.gson.Gson

class TrackRepositoryImpl(
    val sharedPrefs: SharedPreferences,
) : TrackRepository {
    companion object {
        const val COUNT_ITEMS = 10
        const val KEY_HISTORY_LIST = "track_list_history"
    }

    private var trackListHistory: TrackListHistory = TrackListHistory(list = mutableListOf())

    init {
        restoreHistoryList()
    }

    private fun restoreHistoryList() {
        val json = sharedPrefs.getString(KEY_HISTORY_LIST, null)
        if (!json.isNullOrEmpty()) {
            val history = Gson().fromJson(json, TrackListHistory::class.java)
            if (!history.list.isNullOrEmpty()) {
                trackListHistory.list.addAll(history.list)
            }
        }
    }

    override fun addTrackListHistory(track: TrackData) {
        if (trackListHistory.list.contains(track)) {
            trackListHistory.list.remove(track)
        }
        trackListHistory.list.add(0, track)
        if (trackListHistory.list.size > COUNT_ITEMS) {
            trackListHistory.list.removeAt(trackListHistory.list.lastIndex)
        }
    }

    override fun getListHistory(): TrackListHistory {
        return trackListHistory
    }

    override fun clearHistory() {
        sharedPrefs.edit()
            .remove(KEY_HISTORY_LIST)
            .apply()
        trackListHistory.list.clear()
    }

    override fun saveSharedPrefs() {
        if (trackListHistory.list.isNotEmpty()) {
            val json = Gson().toJson(trackListHistory)
            sharedPrefs.edit()
                .putString(KEY_HISTORY_LIST, json)
                .apply()
        }
    }
}
