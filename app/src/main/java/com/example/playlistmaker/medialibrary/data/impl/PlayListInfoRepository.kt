package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.flow.Flow

interface PlayListInfoRepository {
    fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary>
}