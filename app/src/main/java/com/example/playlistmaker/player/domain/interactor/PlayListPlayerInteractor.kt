package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

interface PlayListPlayerInteractor {
    suspend fun getPlayList(): Flow<List<PlayListWithTrack>>
    suspend fun updatePlayList(playerList: PlayerList)
    suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain)
    suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefDomain)
}