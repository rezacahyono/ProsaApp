package com.rchyn.prosa.domain.repository.place

import com.rchyn.prosa.domain.model.place.Place
import kotlinx.coroutines.flow.Flow
import com.rchyn.prosa.utils.Result


interface PlaceRepository {

    fun getPlace(query: String): Flow<Result<List<Place>>>
}