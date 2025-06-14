package com.example.playlistmaker.medialibrary.domain.interactor

import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.flow.Flow

interface PlayListInfoInteractor {
    suspend fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary>
}