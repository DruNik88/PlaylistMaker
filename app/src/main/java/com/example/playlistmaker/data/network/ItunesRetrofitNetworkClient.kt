package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.model.ItunesRequest
import com.example.playlistmaker.data.model.NetworkResponse
import com.example.playlistmaker.data.repository.TrackNetworkClient

class ItunesRetrofitNetworkClient : TrackNetworkClient {
    override fun doRequest(request: ItunesRequest): NetworkResponse {
        return try {
            val response = RetrofitClient.api.search(request.expression).execute()
            val networkResponse = response.body() ?: NetworkResponse()
            Log.d("networkResponse.night","$networkResponse")
            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCode = 400 }
        }
    }
}