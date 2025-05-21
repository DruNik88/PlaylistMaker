package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.application.db.AppDatabase
import com.example.playlistmaker.application.db.entity.TrackEntity
import com.example.playlistmaker.application.db.mapper.DataBaseConvertor
import com.example.playlistmaker.medialibrary.data.FavouriteRepository
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavouriteRepositoryImpl(
    private val dataBase: AppDatabase,
    private val dataBaseConvertor: DataBaseConvertor
) : FavouriteRepository {
    override fun getTrackListInFavourite(): Flow<List<TrackFavourite>> = flow {
        val trackListEntity = withContext(Dispatchers.IO) {
            dataBase.getTrackDao().getTrackListEntity()
        }
        val listFavouriteTrack = converterTrackEntityToTrackFavourite(trackListEntity)
        emit(listFavouriteTrack)
    }

    private fun converterTrackEntityToTrackFavourite(trackListEntity: List<TrackEntity>): List<TrackFavourite> {
        return dataBaseConvertor.converterTrackEntityToTrackFavourite(trackListEntity)
    }

}