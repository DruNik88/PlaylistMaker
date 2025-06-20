package com.example.playlistmaker.search.data.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.NetworkResponse
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.TrackNetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItunesRetrofitNetworkClient(
    private val itunesApi: ItunesApi,
    private val context: Context
) : TrackNetworkClient {

    override suspend fun doRequest(request: Any): NetworkResponse {

        if (!isConnected()) {
            return NetworkResponse().apply { resultCode = -1 }
        }

        return when (request) {
            is ItunesRequest -> {
                runCatching {
                    withContext(Dispatchers.IO) {
                        itunesApi.search(request.expression)
                    }.apply {
                        resultCode = 200
                    }
                }.getOrElse {
                    NetworkResponse().apply { resultCode = 500 }
                }
            }

            else -> {
                NetworkResponse().apply { resultCode = 400 }
            }
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
