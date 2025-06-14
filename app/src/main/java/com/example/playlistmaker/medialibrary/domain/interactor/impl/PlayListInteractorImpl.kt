package com.example.playlistmaker.medialibrary.domain.interactor.impl

import com.example.playlistmaker.medialibrary.data.PlayListRepository
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playListRepository: PlayListRepository
) : PlayListInteractor {
    override suspend fun getPlayList(): Flow<List<PlayList>> {
        return playListRepository.getPlayList()
    }
}