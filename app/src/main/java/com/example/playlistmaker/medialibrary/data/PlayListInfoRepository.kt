package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.flow.Flow

interface PlayListInfoRepository {
    fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary>
    suspend fun  deleteTrackFromPlaylist(
        crossRef: PlayListTrackCrossRefMediaLibraryDomain,
        track: TrackMediaLibraryDomain
    )
    fun sharePlayList(infoPlaylist: String)
}