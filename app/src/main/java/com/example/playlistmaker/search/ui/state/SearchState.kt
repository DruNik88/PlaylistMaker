package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.ui.view_model.SearchViewModel


sealed interface SearchState {

    data object Loading : SearchState

    data class Content(
//        val trackList: TrackSearchListDomain
        val trackList: List<TrackSearchDomain>
    ) : SearchState

    data class Error(
        val error: SearchViewModel.ErrorSearch
    ) : SearchState
}