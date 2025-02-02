package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.TrackSearchListDomain
import com.example.playlistmaker.search.ui.view_model.SeachViewModel

sealed interface SearchState {

    data object Loading : SearchState

    data class Content(
        val trackList: TrackSearchListDomain
    ) : SearchState

    data class Error(
        val error: SeachViewModel.ErrorSearch
    ) : SearchState
}