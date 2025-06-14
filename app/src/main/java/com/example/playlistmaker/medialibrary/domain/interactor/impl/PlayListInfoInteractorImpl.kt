package com.example.playlistmaker.medialibrary.domain.interactor.impl

import com.example.playlistmaker.medialibrary.data.impl.PlayListInfoRepository
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInfoInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.flow.Flow

class PlayListInfoInteractorImpl(
    private val playListInfoRepository: PlayListInfoRepository
): PlayListInfoInteractor {
    override suspend fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary> {
        return playListInfoRepository.getPlayListWithTrackDetail(playListId)
    }
}