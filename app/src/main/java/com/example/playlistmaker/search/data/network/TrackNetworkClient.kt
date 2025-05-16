package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.model.NetworkResponse

interface TrackNetworkClient {
    suspend fun doRequest(request: Any): NetworkResponse
}