package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.interactor.PlayListPlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerList
import kotlinx.coroutines.flow.Flow

class PlayListPlayerInteractorImpl(
    private val playListPlayerRepository: PlayListPlayerRepository
):PlayListPlayerInteractor {
    override suspend fun getPlayList(): Flow<List<PlayerList>> {
        return playListPlayerRepository.getPlayList()
    }
}