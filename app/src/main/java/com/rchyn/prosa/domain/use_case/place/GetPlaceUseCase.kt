package com.rchyn.prosa.domain.use_case.place

import com.rchyn.prosa.domain.model.place.Place
import com.rchyn.prosa.domain.repository.place.PlaceRepository
import kotlinx.coroutines.flow.Flow
import com.rchyn.prosa.utils.Result
import javax.inject.Inject

class GetPlaceUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {

    operator fun invoke(query: String): Flow<Result<List<Place>>> = placeRepository.getPlace(query)
}