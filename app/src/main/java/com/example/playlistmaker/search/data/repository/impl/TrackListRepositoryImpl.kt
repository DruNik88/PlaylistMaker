package com.example.playlistmaker.search.data.repository.impl

import android.util.Log
import com.example.playlistmaker.search.data.mapper.TrackListApiInTrackListMapper
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.ItunesResponse
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class TrackListRepositoryImpl(private val trackNetworkClient: TrackNetworkClient) :
    TrackListRepository {

    companion object {
        const val CONNECTION_PROBLEMS = "Проблемы со связью"
    }

    override fun searchTrackList(expression: String): Resource<List<TrackSearchDomain>> {
        val networkResponse = trackNetworkClient.doRequest(ItunesRequest(expression))

        return when (networkResponse.resultCode) {
            -1 -> {
                Resource.Error(CONNECTION_PROBLEMS)
            }

            200 -> {
                networkResponse as ItunesResponse
                val trackList = TrackListApiInTrackListMapper.map(networkResponse.results)
                Resource.Success(trackList)
            }

            else -> {Resource.Error(CONNECTION_PROBLEMS)}

        }
    }
}





