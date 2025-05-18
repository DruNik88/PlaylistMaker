package com.example.playlistmaker.player.data.repository.impl

import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.data.db.mapper.DataBaseConvertor
import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

class TrackFavouriteRepositoryImpl(
    private val database: AppDatabase,
    private val dataBaseConvertor: DataBaseConvertor
): TrackFavouriteRepository {
    override fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        val trackEntity = converterTrackDomainToTrackEntity(trackPlayerDomain)
        database.getTrackDao().insertTrackEntity(trackEntity)
    }

    override fun deleteTrackInFavourite() {
        TODO("Not yet implemented")
    }

    override fun getTrackListInFavourite(): Flow<List<TrackEntity>> {
        TODO("Not yet implemented")
    }

    private fun converterTrackDomainToTrackEntity(trackPlayerDomain: TrackPlayerDomain): TrackEntity{
        return dataBaseConvertor.converterTrackDomainToTrackEntity(trackPlayerDomain)
    }
}