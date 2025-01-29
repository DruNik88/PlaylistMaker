package com.example.playlistmaker.search.data.repository.impl

import com.example.playlistmaker.search.data.mapper.TrackListApiInTrackListMapper
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.ItunesResponse
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.data.repository.TrackListRepository
import com.example.playlistmaker.search.domain.model.TrackSearchDomain

class TrackListRepositoryImpl(private val trackNetworkClient: TrackNetworkClient) :
    TrackListRepository {

        companion object{
            const val NOT_FOUND = 1
            const val CONNECTION_PROBLEMS = 2
        }

    override fun searchTrackList(expression: String): Resource<List<TrackSearchDomain>> {
        val networkResponse = trackNetworkClient.doRequest(ItunesRequest(expression))

        return if (networkResponse.resultCode == 200 && networkResponse is ItunesResponse && networkResponse.resultCount > 0) {
            val trackList = TrackListApiInTrackListMapper.map(networkResponse.results)
            Resource.Success(trackList)
        } else if (
            (networkResponse is ItunesResponse &&
                    (networkResponse.resultCount == 0 || networkResponse.resultCode == 404)
                    ) || networkResponse !is ItunesResponse
        )
        {
            Resource.Error(NOT_FOUND)
        } else {
            Resource.Error(CONNECTION_PROBLEMS)
        }
    }
}





