package com.example.playlistmaker.medialibrary.ui.state

import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary

sealed class PlayListWithTrackDetail {
    data object Loading: PlayListWithTrackDetail()
    data class Content(
        val playListData: PlayListWithTrackMediaLibrary
    ) : PlayListWithTrackDetail()

    data object Empty : PlayListWithTrackDetail()
}