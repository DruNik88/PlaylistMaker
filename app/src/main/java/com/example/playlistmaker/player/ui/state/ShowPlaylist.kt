package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer

sealed class ShowPlaylist {
    data class Content(
        val playListData: List<PlayListWithTrackPlayer>
    ) : ShowPlaylist()

    data object Empty : ShowPlaylist()
}