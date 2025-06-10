package com.example.playlistmaker.player.data.repository.impl

import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import com.example.playlistmaker.player.data.repository.PlayListPlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListPlayerRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val converter: DataBaseConvertor
) : PlayListPlayerRepository {
    override fun getPlayList(): Flow<List<PlayerList>> {
        return database.getPlayListDao().getPlayListEntity()
            .map { list ->
                converter.converterPlayListEntityToPlayerListDomain(list)
            }
    }

}