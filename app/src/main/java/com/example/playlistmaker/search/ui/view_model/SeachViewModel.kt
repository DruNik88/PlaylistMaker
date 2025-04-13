package com.example.playlistmaker.search.ui.view_model

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import com.example.playlistmaker.search.ui.state.HistoryState
import com.example.playlistmaker.search.ui.state.SearchState

class SearchViewModel(
    private val searchInteractor: TrackListInteractor,
    private val getUserHistory: HistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    enum class ErrorSearch {
        NOT_FOUND,
        CONNECTION_PROBLEMS,
        INVISIBLE
    }

    private var trackListHistory = TrackSearchListDomain(list = mutableListOf())

    private val stateSearchLiveData = MutableLiveData<SearchState>()
    fun observeStateSearch(): MutableLiveData<SearchState> = stateSearchLiveData

    private val stateHistoryLiveData = MutableLiveData<HistoryState>()
    fun observeHistorySearch(): MutableLiveData<HistoryState> = stateHistoryLiveData

    private var latestRequestText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchRequestText(requestText: String) {

        if (latestRequestText == requestText) {
            return
        }

        latestRequestText = requestText
        request(requestText)

    }

    fun request(requestText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { requestTrack(requestText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
        }

    fun addTrackListHistory(track: TrackSearchDomain) {
        getUserHistory.addTrackListHistory(track)
    }

    fun saveSharedPrefs() {
        getUserHistory.saveSharedPrefs()
    }

    fun getHistoryList() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        trackListHistory = getUserHistory.getListHistory()
        if (trackListHistory.list.isEmpty()) {
            renderStateHistory(HistoryState.Empty)
        } else {
            renderStateHistory(HistoryState.Content(trackListHistory))
        }
    }

    fun clearHistoryList() {
        getUserHistory.clearHistory()
        trackListHistory.list.clear()
        if (trackListHistory.list.isEmpty()) {
            renderStateHistory(HistoryState.Clear)
        }
    }

    private fun requestTrack(requestText: String) {
        if (requestText.isNotEmpty()) {
            renderStateSearch(SearchState.Loading)
        }
        searchInteractor.searchTrackList(
            expression = requestText,
            consumer = object : TrackListInteractor.TrackListConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(trackList: Resource<List<TrackSearchDomain>>) {

                    when (trackList) {
                        is Resource.Error -> {
                            renderStateSearch(
                                SearchState.Error(
                                    error = ErrorSearch.CONNECTION_PROBLEMS
                                )
                            )
                        }

                        is Resource.Success -> {
                            val trackListResponse = TrackSearchListDomain(list = mutableListOf())
                            if (trackList.data != null) {
                                trackListResponse.list.clear()
                                trackListResponse.list.addAll(trackList.data.toMutableList())
                            }
                            when {
                                trackListResponse.list.isEmpty() -> {
                                    renderStateSearch(
                                        SearchState.Error(
                                            error = ErrorSearch.NOT_FOUND
                                        )
                                    )
                                }

                                else -> {

                                    renderStateSearch(
                                        SearchState.Content(
                                            trackList = trackListResponse
                                        )
                                    )
                                }
                            }

                        }
                    }

                }
            }
        )
    }

    private fun renderStateSearch(state: SearchState) {
        stateSearchLiveData.postValue(state)
    }

    private fun renderStateHistory(state: HistoryState) {
        stateHistoryLiveData.postValue(state)
    }

}