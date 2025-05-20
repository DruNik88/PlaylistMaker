package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.application.debounce
import com.example.playlistmaker.search.domain.interactor.HistoryInteractor
import com.example.playlistmaker.search.domain.interactor.TrackListInteractor
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import com.example.playlistmaker.search.ui.state.HistoryState
import com.example.playlistmaker.search.ui.state.SearchState
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

//    private var trackListHistory = TrackSearchListDomain(list = mutableListOf())
private var trackListHistory: List<TrackSearchDomain> = listOf()

    private var trackListResponse: List<TrackSearchDomain> = listOf()

    private val stateSearchLiveData = MutableLiveData<SearchState>()
    fun observeStateSearch(): MutableLiveData<SearchState> = stateSearchLiveData

    private val stateHistoryLiveData = MutableLiveData<HistoryState>()
    fun observeHistorySearch(): MutableLiveData<HistoryState> = stateHistoryLiveData

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

//    fun getHistoryList() {
//        trackListHistory = getUserHistory.getListHistory()
//        if (trackListHistory.list.isEmpty()) {
//            renderStateHistory(HistoryState.Empty)
//        } else {
//            renderStateHistory(HistoryState.Content(trackListHistory))
//        }
//    }

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
            renderStateHistory(HistoryState.Empty)
        } else {
            renderStateHistory(HistoryState.Content(trackListHistory))
        }
    }


    fun clearHistoryList() {
        getUserHistory.clearHistory()
        trackListHistory = listOf()
        if (trackListHistory.isEmpty()) {
            renderStateHistory(HistoryState.Clear)
        }
    }

    private fun requestTrack(requestText: String) {
        if (requestText.isNotEmpty()) {
            renderStateSearch(SearchState.Loading)
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
                renderStateSearch(
                    SearchState.Error(
                        error = ErrorSearch.CONNECTION_PROBLEMS
                    )
                )
            }

            is Resource.Success -> {
//                val trackListResponse = TrackSearchListDomain(list = mutableListOf())

                trackListResponse = trackList.data?.let{it} ?: listOf()
//                if (trackList.data != null) {
//                    trackListResponse.clear()
//                    trackListResponse.addAll(trackList.data.toMutableList())

//                    trackListResponse.list.clear()
//                    trackListResponse.list.addAll(trackList.data.toMutableList())

                when {
//                    trackListResponse.list.isEmpty() -> {
                    trackListResponse.isEmpty() -> {
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

    private fun renderStateSearch(state: SearchState) {
        stateSearchLiveData.postValue(state)
    }

    private fun renderStateHistory(state: HistoryState) {
        stateHistoryLiveData.postValue(state)
    }

}