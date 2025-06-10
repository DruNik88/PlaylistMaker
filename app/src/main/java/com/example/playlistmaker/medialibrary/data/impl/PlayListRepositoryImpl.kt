package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.PlayListRepository
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val converter: DataBaseConvertor,
    private val dataBase: DatabasePlayListEntity
): PlayListRepository {

    override fun getPlayList(): Flow<List<PlayList>> {
        return dataBase.getPlayListDao().getPlayListEntity()
            .map { list ->
                converter.converterPlayListEntityToPlayListDomain(list)
            }
    }
}