package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.model.ItunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun search(@Query("term") text: String): ItunesResponse

//    @GET("search")
//    fun search(@Query("term") text: String): Call<ItunesResponse>
}
