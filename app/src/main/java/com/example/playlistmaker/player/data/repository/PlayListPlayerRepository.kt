package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefPlayerDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface PlayListPlayerRepository {
    fun getPlayList(): Flow<List<PlayListWithTrackPlayer>>
    suspend fun updatePlayList(playerList: PlayerList, track: TrackPlayerDomain)
    suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain)
    suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefPlayerDomain)
}