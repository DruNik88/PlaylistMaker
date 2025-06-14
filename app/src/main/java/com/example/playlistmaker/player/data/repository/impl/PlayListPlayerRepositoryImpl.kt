package com.example.playlistmaker.player.data.repository.impl

import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrackPlayer
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import com.example.playlistmaker.search.data.model.TrackSearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListPlayerRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val converter: DataBaseConvertor
) : PlayListPlayerRepository {
    override fun getPlayList(): Flow<List<PlayListWithTrackPlayer>> {
        return database.getPlayListDao().getPlayListWithTrackEntity()
            .map { list ->
                converter.converterPlayListWithTrackEntityToPlayListWithTrack(list)
            }
    }

    override suspend fun updatePlayList(playerList: PlayerList, track: TrackPlayerDomain) {
        withContext(Dispatchers.IO) {
            val durationTrack = converterTime(track)
            val playListEntity = converter.converterPlayerListDomainToPlayListEntity(playerList)
            val newPlayListEntity = playListEntity.copy(
                countTrack = playListEntity.countTrack + 1,
                durationPlayList = playListEntity.durationPlayList + durationTrack)
            database.getPlayListDao().updatePlayListEntity(newPlayListEntity)
        }
    }

    override suspend fun addTrackInTrackInPlayListTable(track: TrackPlayerDomain) {
        withContext(Dispatchers.IO) {
            val trackEntity = converter.converterTrackDomainToTrackEntity(track)
            database.getPlayListDao().insertTrackEntity(trackEntity)
        }
    }

    override suspend fun addPlayListTrackCrossRef(playListTrackCrossRefDomain: PlayListTrackCrossRefDomain) {
        withContext(Dispatchers.IO) {
            val playListTrackCrossRef =
                converter.playListTrackCrossRefDomainToPlayListTrackCrossRef(
                    playListTrackCrossRefDomain
                )
            database.getPlayListDao().addPlayListTrackCrossRef(playListTrackCrossRef)
        }
    }

    private fun converterTime(track: TrackPlayerDomain): Int {
        val trackTime = track.trackTimeMillis
        val listTime = trackTime?.split(":")
        val minuteToSeconds = listTime?.get(0)?.toInt()?.times(60) ?: 0
        val seconds = listTime?.get(1)?.toInt() ?: 0
        val durationTrackInSeconds = minuteToSeconds + seconds
        return durationTrackInSeconds
    }

}