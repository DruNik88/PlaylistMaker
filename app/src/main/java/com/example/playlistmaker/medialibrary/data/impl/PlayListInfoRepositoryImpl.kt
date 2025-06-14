package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.application.db.DatabasePlayListEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.domain.model.PlayListWithTrackMediaLibrary
import com.example.playlistmaker.medialibrary.ui.state.PlayListWithTrackDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayListInfoRepositoryImpl(
    val database: DatabasePlayListEntity,
    private val convertor: DataBaseConvertor
): PlayListInfoRepository {
    override fun getPlayListWithTrackDetail(playListId: Long): Flow<PlayListWithTrackMediaLibrary> {
            return database.getPlayListDao().getPlayListWithTrackDetailEntity(playListId).map { playList ->
                convertor.converterPlayListWithTrackEntityToPlayListWithTrack(playList)
            }

    }
}