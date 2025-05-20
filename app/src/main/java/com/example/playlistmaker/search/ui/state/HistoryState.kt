package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.TrackSearchDomain

sealed interface HistoryState {
    data class Content(
        val trackList: List<TrackSearchDomain>
    ) : HistoryState

    data object Empty : HistoryState
    data object Clear :HistoryState
}