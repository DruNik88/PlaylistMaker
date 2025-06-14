package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface PlayListPlayerRepository {
    fun getPlayList(): Flow<List<PlayListWithTrack>>
    suspend fun updatePlayList(playerList: PlayerList)
    suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain)
    suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefDomain)
}