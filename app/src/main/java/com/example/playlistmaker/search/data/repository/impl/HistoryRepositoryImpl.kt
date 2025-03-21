package com.example.playlistmaker.search.data.repository.impl

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.mapper.TrackOrListMapper
import com.example.playlistmaker.search.data.model.TrackListHistory
import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.google.gson.Gson

class HistoryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences,
) : HistoryRepository {
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
            val history = gson.fromJson(json, TrackListHistory::class.java)
            if (!history.list.isNullOrEmpty()) {
                trackListHistory.list.addAll(history.list)
            }
        }
    }

    override fun addTrackListHistory(track: TrackSearchDomain) {
        val trackData = TrackOrListMapper.trackDomaInToTrackData(track)
        if (trackListHistory.list.contains(trackData)) {
            trackListHistory.list.remove(trackData)
        }
        trackListHistory.list.add(0, trackData)
        if (trackListHistory.list.size > COUNT_ITEMS) {
            trackListHistory.list.removeAt(trackListHistory.list.lastIndex)
        }
    }

    override fun getListHistory(): com.example.playlistmaker.search.domain.model.TrackSearchListDomain {
        val listDomain = TrackOrListMapper.listDataToListDomain(trackListHistory)
        return listDomain
    }

    override fun clearHistory() {
        sharedPrefs.edit()
            .remove(KEY_HISTORY_LIST)
            .apply()
        trackListHistory.list.clear()
    }

    override fun saveSharedPrefs() {
        if (trackListHistory.list.isNotEmpty()) {
            val json = gson.toJson(trackListHistory)
            sharedPrefs.edit()
                .putString(KEY_HISTORY_LIST, json)
                .apply()
        }
    }
}

