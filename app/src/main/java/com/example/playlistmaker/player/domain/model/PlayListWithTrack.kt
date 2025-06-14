package com.example.playlistmaker.player.domain.model

data class PlayListWithTrack(
    val playList: PlayerList,
    val trackList: List<TrackPlayerDomain>
)
