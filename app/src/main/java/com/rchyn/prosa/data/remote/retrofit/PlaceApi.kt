package com.rchyn.prosa.data.remote.retrofit

import com.rchyn.prosa.data.remote.dto.place.ResponsePlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApi {

    @GET("geocode/autocomplete")
    suspend fun getPlace(
        @Query("text") query: String,
        @Query("apiKey") key: String
    ): Response<ResponsePlace>
}