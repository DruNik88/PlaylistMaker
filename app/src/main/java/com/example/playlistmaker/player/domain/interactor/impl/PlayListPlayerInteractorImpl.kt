package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

class PlayListPlayerInteractorImpl(
    private val playListPlayerRepository: PlayListPlayerRepository
) : PlayListPlayerInteractor {
    override suspend fun getPlayList(): Flow<List<PlayListWithTrack>> {
        return playListPlayerRepository.getPlayList()
    }

    override suspend fun updatePlayList(playerList: PlayerList) {
        playListPlayerRepository.updatePlayList(playerList)
    }

    override suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain) {
        playListPlayerRepository.addTrackInTrackInPlayListTable(track)
    }

    override suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefDomain) {
        playListPlayerRepository.addPlayListTrackCrossRef(playListTrackCrossRefDomain)
    }
}