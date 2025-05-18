package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.data.db.entity.TrackEntity
import com.example.playlistmaker.player.data.repository.TrackFavouriteRepository
import com.example.playlistmaker.player.domain.interactor.TrackFavouriteInteractor
import com.example.playlistmaker.player.domain.model.TrackPlayerDomain
import kotlinx.coroutines.flow.Flow

class TrackFavouriteInteractorImpl (
    private val trackFavouriteRepository: TrackFavouriteRepository
): TrackFavouriteInteractor {
    override suspend fun insertTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
       trackFavouriteRepository.insertTrackInFavourite(trackPlayerDomain)
    }

    override suspend fun deleteTrackInFavourite(trackPlayerDomain: TrackPlayerDomain) {
        trackFavouriteRepository.deleteTrackInFavourite(trackPlayerDomain)
    }

    override fun getTrackListInFavourite(): Flow<List<TrackEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrackListIdEntity(): Flow<List<Long>> {
        return trackFavouriteRepository.getTrackListIdEntity()
    }

}