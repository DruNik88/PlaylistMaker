package com.example.playlistmaker.search.data.repository.impl

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
        const val NOT_FOUND = 1
        const val CONNECTION_PROBLEMS = 2
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

            else -> {Resource.Error(NOT_FOUND)}
        }
    }
}





