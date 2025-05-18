package com.example.playlistmaker.search.data.repository.impl

import android.util.Log
import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.search.data.mapper.TrackListApiInTrackListMapper
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.ItunesResponse
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackListRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient,
    private val database: AppDatabase,) :
    TrackListRepository {

    companion object {
        const val CONNECTION_PROBLEMS = "Проблемы со связью"
    }

    override fun searchTrackList(expression: String): Flow<Resource<List<TrackSearchDomain>>> = flow {
        val networkResponse = trackNetworkClient.doRequest(ItunesRequest(expression))

        when (networkResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(CONNECTION_PROBLEMS))
            }

            200 -> {
               val listId = withContext(Dispatchers.IO){
                    database.getTrackDao().getTrackListIdEntity()
                }.toSet()

                networkResponse as ItunesResponse
                val trackList = TrackListApiInTrackListMapper.map(networkResponse.results)
                trackList.forEach() { track -> track.isFavourite = track.trackId in listId
                }
                emit(Resource.Success(trackList))
            }

            else -> {emit(Resource.Error(CONNECTION_PROBLEMS))}

        }
    }
}





