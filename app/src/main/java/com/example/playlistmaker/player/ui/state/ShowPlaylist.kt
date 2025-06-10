package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.PlayerList

sealed class ShowPlaylist {
    data class Content(
        val playListData: List<PlayerList>
    ): ShowPlaylist()
    data object Empty : ShowPlaylist()
}