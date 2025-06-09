package com.example.playlistmaker.medialibrary.ui.state

import com.example.playlistmaker.medialibrary.domain.model.PlayList

sealed class NewPlayListData {
    data class Content(
        val newPlayListData: PlayList
    ): NewPlayListData()
    data object Empty : NewPlayListData()
}
