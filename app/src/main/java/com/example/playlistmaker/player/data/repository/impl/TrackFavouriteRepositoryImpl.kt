package com.example.playlistmaker.player.data.repository.impl

import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.data.db.mapper.DataBaseConvertor
import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackFavouriteRepositoryImpl(
    private val database: AppDatabase,
    private val dataBaseConvertor: DataBaseConvertor
): TrackFavouriteRepository {
    override suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        val trackEntity = converterTrackDomainToTrackEntity(trackPlayerDomain)
        withContext(Dispatchers.IO) { database.getTrackDao().insertTrackEntity(trackEntity) }
    }

    override suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        val trackEntity = converterTrackDomainToTrackEntity(trackPlayerDomain)
        withContext(Dispatchers.IO) {database.getTrackDao().deleteTrackEntity(trackEntity)}
    }

    override fun getTrackListInFavourite(): Flow<List<TrackEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrackListIdEntity(): Flow<List<Long>> = flow {
        withContext(Dispatchers.IO){
            val listIdBd = database.getTrackDao().getTrackListIdEntity()
            emit(listIdBd)
        }
    }


    private fun converterTrackDomainToTrackEntity(trackPlayerDomain: TrackPlayerDomain): TrackEntity{
        return dataBaseConvertor.converterTrackDomainToTrackEntity(trackPlayerDomain)
    }
}