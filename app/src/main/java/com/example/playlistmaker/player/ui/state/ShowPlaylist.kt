package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList

sealed class ShowPlaylist {
    data class Content(
        val playListData: List<PlayListWithTrack>
    ): ShowPlaylist()
    data object Empty : ShowPlaylist()
}