package com.example.playlistmaker.player.data.repository.impl

import android.util.Log
import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.model.PlayListTrackCrossRefDomain
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayListPlayerRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val converter: DataBaseConvertor
) : PlayListPlayerRepository {
    override fun getPlayList(): Flow<List<PlayListWithTrack>> {
        return database.getPlayListDao().getPlayListWithTrackEntity()
            .map { list ->
                converter.converterPlayListWithTrackEntityToPlayListWithTrack(list)
            }
    }

    override suspend fun updatePlayList(playerList: PlayerList) {
        withContext(Dispatchers.IO) {
            val playListEntity = converter.converterPlayerListDomainToPlayListEntity(playerList)
            val newPlayListEntity = playListEntity.copy(countTrack = playListEntity.countTrack + 1)
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

}