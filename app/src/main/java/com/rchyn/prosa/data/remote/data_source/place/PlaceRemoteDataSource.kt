package com.rchyn.prosa.data.remote.data_source.place

import com.rchyn.prosa.data.remote.retrofit.PlaceApi
import com.rchyn.prosa.utils.handleApi
import javax.inject.Inject

class PlaceRemoteDataSource @Inject constructor(
    private val placeApi: PlaceApi
) {

    suspend fun getPlace(query: String, key: String) =
        handleApi { placeApi.getPlace(query, key) }

}