package com.example.playlistmaker.medialibrary.domain.interactor.impl

import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.medialibrary.data.PlayListInfoRepository
import com.example.playlistmaker.medialibrary.domain.interactor.PlayListInfoInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.flow.Flow

class PlayListInfoInteractorImpl(
    private val playListInfoRepository: PlayListInfoRepository
) : PlayListInfoInteractor {
    override suspend fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary> {
        return playListInfoRepository.getPlayListWithTrackDetail(playListId)
    }

    override suspend fun deleteTrackFromPlaylist(
        crossRef: PlayListTrackCrossRefMediaLibraryDomain,
        track: TrackMediaLibraryDomain
    ) {
        playListInfoRepository.deleteTrackFromPlaylist(crossRef, track)
    }

    override fun sharePlayList(playListWithTrackMediaLibrary: PlayListWithTrackMediaLibrary) {


        val infoPlaylist = buildString {
            appendLine(playListWithTrackMediaLibrary.playList.title)
            appendLine(playListWithTrackMediaLibrary.playList.description)
            appendLine(trackEndings(playListWithTrackMediaLibrary.playList.count))
            appendLine(playListWithTrackMediaLibrary.trackList.mapIndexed { index, trackList ->
                "${index + 1}. ${trackList.artistName} - ${trackList.trackName} (${trackList.trackTimeMillis})"
            })
        }
        playListInfoRepository.sharePlayList(infoPlaylist)
    }

    override suspend fun deletePlayList(playList: PlayList) {
        playListInfoRepository.deletePlayList(playList)
    }
}