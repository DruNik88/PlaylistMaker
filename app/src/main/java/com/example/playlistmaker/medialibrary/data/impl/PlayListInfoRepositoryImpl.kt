package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.PlayListInfoRepository
import com.example.playlistmaker.medialibrary.domain.model.PlayListTrackCrossRefMediaLibraryDomain
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayListInfoRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val convertor: DataBaseConvertor
) : PlayListInfoRepository {
    override fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary> {
        return database.getPlayListDao().getPlayListWithTrackDetailEntity(playListId)
            .map { playList ->
                convertor.converterPlayListWithTrackEntityToPlayListWithTrack(playList)
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

    private fun converterTime(track: TrackMediaLibraryDomain): Int {
        val trackTime = track.trackTimeMillis
        val listTime = trackTime?.split(":")
        val minuteToSeconds = listTime?.get(0)?.toInt()?.times(60) ?: 0
        val seconds = listTime?.get(1)?.toInt() ?: 0
        val durationTrackInSeconds = minuteToSeconds + seconds
        return durationTrackInSeconds
    }
}

