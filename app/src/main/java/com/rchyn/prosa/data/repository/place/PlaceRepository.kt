package com.rchyn.prosa.data.repository.place

import com.rchyn.prosa.model.place.Place
import com.rchyn.prosa.utils.Result
import kotlinx.coroutines.flow.Flow


interface PlaceRepository {

    fun getPlace(query: String): Flow<Result<List<Place>>>
}