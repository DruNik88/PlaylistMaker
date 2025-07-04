package com.example.playlistmaker.medialibrary.data.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.application.converterTime
import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.PlayListInfoRepository
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class PlayListInfoRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val convertor: DataBaseConvertor,
    private val context: Context
) : PlayListInfoRepository {

    companion object {
        private const val MIME_TYPE = "text/plain"
    }

    override fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary> {
        val playListFlow = database.getPlayListDao().getPlayListWithTrackDetailEntity(playListId)
        val sortedTracksFlow = database.getPlayListDao().getSortedTracksForPlaylistFlow(playListId)


        return combine(playListFlow, sortedTracksFlow) { playlistWithTracks, sortedTracks ->
            val playListWithTrackMediaLibrary =
                convertor.converterPlayListWithTrackEntityToPlayListWithTrack(playlistWithTracks)
            val sortedList = sortedTracks.map { list ->
                convertor.converterTrackEntityToTrackMediaLibraryDomain(list)
            }
            playListWithTrackMediaLibrary.copy(trackList = sortedList)
        }
    }

    override suspend fun deleteTrackFromPlaylist(
        crossRef: PlayListTrackCrossRefMediaLibraryDomain,
        track: TrackMediaLibraryDomain
    ) {
        val tracDuration = converterTime(track)
        val playListTrackCrossRef =
            convertor.playListTrackCrossRefMediaLibraryDomainToPlayListTrackCrossRef(crossRef)
        val trackId = playListTrackCrossRef.trackId
        val playListId = playListTrackCrossRef.playlistId
        withContext(Dispatchers.IO) {
            database.getPlayListDao().deleteTrackFromPlaylist(playListTrackCrossRef)
            database.getPlayListDao().updateCountTrackAndDurationPlayList(playListId, tracDuration)
            val crossRefEntity = database.getPlayListDao().getCrossRefsByTrackId(trackId)
            if (crossRefEntity.isEmpty()) {
                database.getPlayListDao().deleteTrack(trackId)
            }
        }
    }

    override fun sharePlayList(infoPlaylist: String) {

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, infoPlaylist)
            type = MIME_TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    override suspend fun deletePlayList(playList: PlayList) {
        val playListEntity = convertor.converterPlayListDomainToPlayListEntity(playList)
        withContext(Dispatchers.IO) {
            database.getPlayListDao().deletePlayList(playListEntity)
        }
    }
}

