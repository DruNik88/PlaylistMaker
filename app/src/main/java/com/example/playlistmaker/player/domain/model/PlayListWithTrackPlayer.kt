package com.example.playlistmaker.player.domain.model

data class PlayListWithTrackPlayer(
    val playList: PlayerList,
    val trackList: List<TrackPlayerDomain>
)
