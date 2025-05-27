package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

sealed class SearchHistoryState {

    data object SearchLoading : SearchHistoryState()

    data class SearchContent(
        val trackList: List<TrackSearchDomain>
    ) : SearchHistoryState()

    data class SearchError(
        val error: SearchViewModel.ErrorSearch
    ) : SearchHistoryState()

    data class HistoryContent(
        val trackList: List<TrackSearchDomain>
    ) : SearchHistoryState()

    data object HistoryEmpty : SearchHistoryState()
    data object HistoryClear :SearchHistoryState()
}