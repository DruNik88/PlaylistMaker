package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.application.db.DatabaseTrackEntity
import com.example.playlistmaker.application.db.entity.TrackEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteRepositoryImpl(
    private val dataBase: DatabaseTrackEntity,
    private val dataBaseConvertor: DataBaseConvertor
) : FavouriteRepository {

    override fun getTrackListInFavourite(): Flow<List<TrackFavourite>> {
        return dataBase.getTrackDao().getTrackListEntity()
            .map { list ->
                val sortedList = list.sortedByDescending { it.timeAdded }
                converterTrackEntityToTrackFavourite(sortedList)
            }
    }

    private fun converterTrackEntityToTrackFavourite(trackListEntity: List<TrackEntity>): List<TrackFavourite> {
        return dataBaseConvertor.converterTrackEntityToTrackFavourite(trackListEntity)
    }

}