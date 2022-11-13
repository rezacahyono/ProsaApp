package com.rchyn.prosa.data.repository.place

import com.rchyn.prosa.BuildConfig
import com.rchyn.prosa.data.remote.data_source.place.PlaceRemoteDataSource
import com.rchyn.prosa.data.toPlace
import com.rchyn.prosa.domain.model.place.Place
import com.rchyn.prosa.domain.repository.place.PlaceRepository
import com.rchyn.prosa.utils.ApiResult
import com.rchyn.prosa.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceRepository {
    override fun getPlace(query: String): Flow<Result<List<Place>>> = flow {
        val apiKey = BuildConfig.GEOAPIFY_KEY

        when (val source = placeRemoteDataSource.getPlace(query, apiKey)) {
            is ApiResult.ApiSuccess -> {
                val result = source.data.features.map { it.toPlace() }
                emit(Result.Success(result))
            }
            is ApiResult.ApiError -> {
                emit(Result.Error(source.uiText))
            }
        }
    }.onStart { Result.Loading }.flowOn(ioDispatcher)


}