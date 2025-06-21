package com.example.playlistmaker.medialibrary.domain.interactor

import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.flow.Flow

interface PlayListInfoInteractor {
    suspend fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary>
    suspend fun deleteTrackFromPlaylist(
        crossRef: PlayListTrackCrossRefMediaLibraryDomain,
        track: TrackMediaLibraryDomain
    )

//    fun sharePlayList(infoPlaylist: String)

    fun sharePlayList(playListWithTrackMediaLibrary: PlayListWithTrackMediaLibrary)

    suspend fun deletePlayList(playList: PlayList)
}