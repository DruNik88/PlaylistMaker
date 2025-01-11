package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.ItunesRequest
import com.example.playlistmaker.data.model.NetworkResponse

interface TrackNetworkClient {
    fun doRequest(request: ItunesRequest): NetworkResponse
}