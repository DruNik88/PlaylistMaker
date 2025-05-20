package com.example.playlistmaker.search.data.repository.impl

import android.content.SharedPreferences
import com.example.playlistmaker.application.db.AppDatabase
import com.example.playlistmaker.search.data.mapper.TrackOrListMapper
import com.example.playlistmaker.search.data.model.TrackHistory
import com.example.playlistmaker.search.data.model.TrackListHistory
import com.example.playlistmaker.search.data.repository.HistoryRepository
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class HistoryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences,
    private val database: AppDatabase
) : HistoryRepository {
    companion object {
        const val COUNT_ITEMS = 10
        const val KEY_HISTORY_LIST = "track_list_history"
    }

//    private var trackListHistory: TrackListHistory = TrackListHistory(list = mutableListOf())
    private var trackListHistory: MutableList<TrackHistory> = mutableListOf()

    init {
        restoreHistoryList()
    }

    private fun restoreHistoryList() {
        val json = sharedPrefs.getString(KEY_HISTORY_LIST, null)
        if (!json.isNullOrEmpty()) {
            val history = gson.fromJson(json, TrackListHistory::class.java)
            if (!history.list.isNullOrEmpty()) {
                trackListHistory.addAll(history.list)
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

//    override fun addTrackListHistory(track: TrackSearchDomain) {
//        val trackData = TrackOrListMapper.trackDomaInToTrackData(track)
//        if (trackListHistory.list.contains(trackData)) {
//            trackListHistory.list.remove(trackData)
//        }
//        trackListHistory.list.add(0, trackData)
//        if (trackListHistory.list.size > COUNT_ITEMS) {
//            trackListHistory.list.removeAt(trackListHistory.list.lastIndex)
//        }
//    }

    override fun getListHistory(): Flow<List<TrackSearchDomain>> = flow {
        val listId = withContext(Dispatchers.IO){
            database.getTrackDao().getTrackListIdEntity()
        }
//        val listDomain = TrackOrListMapper.listDataToListDomain(trackListHistory)
//        listDomain.list.forEach{ track -> track.isFavourite = track.trackId in listId}


        trackListHistory.forEach{ track ->
            track.isFavourite = track.trackId in listId}
        val listDomain = TrackOrListMapper.listDataToListDomain(trackListHistory)

        emit(listDomain)
    }

//    override fun getListHistory(): com.example.playlistmaker.search.domain.model.TrackSearchListDomain {
//        val listDomain = TrackOrListMapper.listDataToListDomain(trackListHistory)
//        return listDomain
//    }

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

