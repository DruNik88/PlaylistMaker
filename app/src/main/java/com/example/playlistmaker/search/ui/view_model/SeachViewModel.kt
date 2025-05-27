package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.state.SearchHistoryState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: TrackListInteractor,
    private val getUserHistory: HistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    enum class ErrorSearch {
        NOT_FOUND,
        CONNECTION_PROBLEMS,
        INVISIBLE
    }

    private var trackListHistory: List<TrackSearchDomain> = listOf()

    private var trackListResponse: List<TrackSearchDomain> = listOf()

    private val stateSearchHistoryLiveData = MutableLiveData<SearchHistoryState>()
    fun observeSearchHistory(): MutableLiveData<SearchHistoryState> = stateSearchHistoryLiveData

    private var latestRequestText: String? = null

    private val trackSearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true,
    ) { requestText ->
        requestTrack(requestText)
    }

    fun searchRequestText(requestText: String) {
        if (latestRequestText == requestText) {
            return
        }

        latestRequestText = requestText
        request(requestText)
    }

    fun request(requestText: String) {
        trackSearchDebounce(requestText)
    }

    fun addTrackListHistory(track: TrackSearchDomain) {
        getUserHistory.addTrackListHistory(track)
    }

    fun saveSharedPrefs() {
        getUserHistory.saveSharedPrefs()
    }

    fun getHistoryList() {
        viewModelScope.launch {
            getUserHistory.getListHistory().collect { trackList ->
                showHistoryList(trackList)
            }
        }
    }

    private fun showHistoryList(trackList: List<TrackSearchDomain>) {
        trackListHistory = trackList
        if (trackListHistory.isEmpty()) {
            renderSearchHistoryState(SearchHistoryState.HistoryEmpty)
        } else {
            renderSearchHistoryState(SearchHistoryState.HistoryContent(trackListHistory))
        }
    }

    fun clearHistoryList() {
        getUserHistory.clearHistory()
        trackListHistory = listOf()
        if (trackListHistory.isEmpty()) {
            renderSearchHistoryState(SearchHistoryState.HistoryClear)
        }
    }

    private fun requestTrack(requestText: String) {
        if (requestText.isNotEmpty()) {
            renderSearchHistoryState(SearchHistoryState.SearchLoading)
            viewModelScope.launch {
                searchInteractor.searchTrackList(requestText)
                    .collect { trackList ->
                        showRequest(trackList)
                    }
            }
        }
    }

    private fun showRequest(trackList: Resource<List<TrackSearchDomain>>) {
        when (trackList) {
            is Resource.Error -> {
                renderSearchHistoryState(
                    SearchHistoryState.SearchError(
                        error = ErrorSearch.CONNECTION_PROBLEMS
                    )
                )
            }

            is Resource.Success -> {

                trackListResponse = trackList.data?.let { it } ?: listOf()

                when {
                    trackListResponse.isEmpty() -> {
                        renderSearchHistoryState(
                            SearchHistoryState.SearchError(
                                error = ErrorSearch.NOT_FOUND
                            )
                        )
                    }

                    else -> {

                        renderSearchHistoryState(
                            SearchHistoryState.SearchContent(
                                trackList = trackListResponse
                            )
                        )
                    }
                }
            }
        }
    }

    private fun renderSearchHistoryState(state: SearchHistoryState) {
        stateSearchHistoryLiveData.postValue(state)
    }
}