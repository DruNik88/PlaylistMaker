package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefPlayerDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

class PlayListPlayerInteractorImpl(
    private val playListPlayerRepository: PlayListPlayerRepository
) : PlayListPlayerInteractor {
    override suspend fun getPlayList(): Flow<List<PlayListWithTrackPlayer>> {
        return playListPlayerRepository.getPlayList()
    }

    override suspend fun updatePlayList(playerList: PlayerList, track: TrackPlayerDomain) {
        playListPlayerRepository.updatePlayList(playerList, track)
    }

    override suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain) {
        playListPlayerRepository.addTrackInTrackInPlayListTable(track)
    }

    override suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefPlayerDomain) {
        playListPlayerRepository.addPlayListTrackCrossRef(playListTrackCrossRefDomain)
    }
}