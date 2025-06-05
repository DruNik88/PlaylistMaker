package com.example.playlistmaker.search.data.repository.impl

import android.content.SharedPreferences
import com.example.playlistmaker.application.db.DatabaseTrackEntity
import com.example.playlistmaker.search.data.mapper.TrackOrListMapper
import com.example.playlistmaker.search.data.model.TrackHistory
import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences,
    private val database: DatabaseTrackEntity
) : HistoryRepository {
    companion object {
        const val COUNT_ITEMS = 10
        const val KEY_HISTORY_LIST = "track_list_history"
    }

    private var trackListHistory: MutableList<TrackHistory> = mutableListOf()

    init {
        restoreHistoryList()
    }

    private fun restoreHistoryList() {
        val json = sharedPrefs.getString(KEY_HISTORY_LIST, null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<TrackHistory>>() {}.type
            val history: MutableList<TrackHistory> = gson.fromJson(json, type)
            if (!history.isNullOrEmpty()) {
                trackListHistory.addAll(history)
            }
        }
    }

    override fun addTrackListHistory(track: TrackSearchDomain) {
        val trackData = TrackOrListMapper.trackDomaInToTrackData(track)
        if (trackListHistory.contains(trackData)) {
            trackListHistory.remove(trackData)
        }
        trackListHistory.add(0, trackData)
        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    override fun getListHistory(): Flow<List<TrackSearchDomain>> = flow {

        if (trackListHistory.isEmpty()) {
            emit(emptyList())
        } else (
                emitAll(
                    database.getTrackDao().getTrackListIdEntity().map { listId ->
                        val favouriteList = trackListHistory.map { trackHistory ->
                            trackHistory.copy(isFavourite = trackHistory.trackId in listId)
                        }.toMutableList()
                        val listDomain = TrackOrListMapper.listDataToListDomain(favouriteList)
                        listDomain
                    }
                )
                )
    }

        override fun clearHistory() {
            sharedPrefs.edit()
                .remove(KEY_HISTORY_LIST)
                .apply()
            trackListHistory.clear()
        }

        override fun saveSharedPrefs() {
            if (trackListHistory.isNotEmpty()) {
                val json = gson.toJson(trackListHistory)
                sharedPrefs.edit()
                    .putString(KEY_HISTORY_LIST, json)
                    .apply()
            }
        }
    }
