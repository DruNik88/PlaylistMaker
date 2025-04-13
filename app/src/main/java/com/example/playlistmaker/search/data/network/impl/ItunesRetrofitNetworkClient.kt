package com.example.playlistmaker.search.data.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.NetworkResponse
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.TrackNetworkClient

class ItunesRetrofitNetworkClient(
    private val itunesApi: ItunesApi,
    private val context: Context
) : TrackNetworkClient {
    override fun doRequest(request: ItunesRequest): NetworkResponse {
        return try {
            if (!isConnected()) {
                return NetworkResponse().apply { resultCode = -1 }
            }
            val response = itunesApi.search(request.expression).execute()
            response.body()?.results?.isEmpty()
            val networkResponse = response.body() ?: NetworkResponse()
            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }
}
