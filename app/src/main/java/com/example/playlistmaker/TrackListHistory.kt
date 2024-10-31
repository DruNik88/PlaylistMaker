package com.example.playlistmaker

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App.Companion.SHARED_PREFERENCES_PLAYLIST_MAKER
import com.google.gson.Gson

const val KEY_HISTORY_LIST = "track_list_history"

class TrackListHistory(context: Context) {
    companion object{
        const val COUNT_ITEMS = 10
    }

    private var trackListHistory: MutableList<Track> = mutableListOf()
    private val sharedPreferences = (context as App).getSharedPreferences(
        SHARED_PREFERENCES_PLAYLIST_MAKER, MODE_PRIVATE
    )
    init {
        restoreHistoryList()
    }

    private fun restoreHistoryList(){
        val json = sharedPreferences.getString(KEY_HISTORY_LIST, null)
        if (!json.isNullOrEmpty()){
            val history = Gson().fromJson(json, Array<Track>::class.java)
            if (!history.isNullOrEmpty()){
                trackListHistory.addAll(history)
            }

        }

    }

    fun addTrackListHistory(track: Track){
        if (trackListHistory.contains(track)){
            trackListHistory.remove(track)
        }
        trackListHistory.add(0, track)
        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    fun saveSharedPrefs(trackList: MutableList<Track>){
        if (trackList.isNotEmpty()){
            val json = Gson().toJson(trackList)
            sharedPreferences.edit()
                .putString(KEY_HISTORY_LIST, json)
                .apply()
        }
    }

    fun clearHistory(){
        sharedPreferences.edit()
            .remove(KEY_HISTORY_LIST)
            .apply()
        trackListHistory.clear()
    }

    fun getListHistory(): MutableList<Track> = trackListHistory

}





private fun createJsonFromTrack(track: Track): String {
    return Gson().toJson(track)
}

private fun createTrackFromJson(json: String): Track {
    return Gson().fromJson(json, Track::class.java)
}
