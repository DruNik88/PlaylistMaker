package com.example.playlistmaker.search.data.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.model.ItunesRequest
import com.example.playlistmaker.search.data.model.NetworkResponse
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.network.TrackNetworkClient

class ItunesRetrofitNetworkClient (private val context: Context): TrackNetworkClient {
    override fun doRequest(request: ItunesRequest): NetworkResponse {
        return try {
            if (!isConnected()){
                return NetworkResponse().apply { resultCode = -1 }
            }
            val response = RetrofitClient.api.search(request.expression).execute()
            val networkResponse = response.body() ?: NetworkResponse()
            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        Log.d("capabilities","$capabilities")
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.d("capabilities_CELLULAR","true")
                    return true}
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.d("capabilities_WIFI","true")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.d("capabilities_ETHERNET","true")
                    return true
                }
            }
        }
        Log.d("capabilities_false","false")
        return false
    }
}



//
//class ItunesRetrofitNetworkClient : TrackNetworkClient {
//    override fun doRequest(request: ItunesRequest): NetworkResponse {
//        return try {
//            val response = RetrofitClient.api.search(request.expression).execute()
//            val networkResponse = response.body() ?: NetworkResponse()
//            Log.d("networkResponse.night","$networkResponse")
//            networkResponse.apply { resultCode = response.code() }
//        } catch (ex: Exception) {
//            return NetworkResponse().apply { resultCode = 400 }
//        }
//    }
//}