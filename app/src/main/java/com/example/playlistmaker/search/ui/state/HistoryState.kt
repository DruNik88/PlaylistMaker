package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.TrackSearchListDomain

sealed interface HistoryState {
    data class Content(
        val trackList: TrackSearchListDomain
    ) : HistoryState

    data object Empty : HistoryState
    data object Clear :HistoryState
}