package com.example.playlistmaker.medialibrary.ui.state

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary

sealed class PlayListWithTrackDetail {
    data object Loading: PlayListWithTrackDetail()
    data class Content(
        val playListData: PlayListWithTrackMediaLibrary
    ) : PlayListWithTrackDetail()

    data class Empty (
        val playList: PlayList
    ): PlayListWithTrackDetail()
}