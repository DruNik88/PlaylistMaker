package com.example.playlistmaker.medialibrary.ui.state

import com.example.playlistmaker.medialibrary.domain.model.PlayList

sealed class PlayListData {
    data class Content(
        val playListData: List<PlayList>
    ): PlayListData()
    data object Empty : PlayListData()
}
