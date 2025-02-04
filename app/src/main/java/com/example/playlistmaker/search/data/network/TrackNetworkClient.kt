package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.NetworkResponse

interface TrackNetworkClient {
    fun doRequest(request: ItunesRequest): NetworkResponse
}