package com.rchyn.prosa.data.repository.stories

import androidx.paging.*
import com.rchyn.prosa.R
import com.rchyn.prosa.data.local.data_source.StoryLocalDataSource
import com.rchyn.prosa.data.local.data_store.UserPrefDataStore
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.remote.data_source.stories.StoriesRemoteDataSource
import com.rchyn.prosa.data.toStory
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val userPrefDataStore: UserPrefDataStore,
    private val storiesLocalDataSource: StoryLocalDataSource,
    private val storiesRemoteDataSource: StoriesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StoriesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(): Flow<PagingData<Story>> = Pager(
        config = PagingConfig(pageSize = 2), remoteMediator = StoriesRemoteMediator(
            userPrefDataStore, storiesLocalDataSource, storiesRemoteDataSource
        ), pagingSourceFactory = {
            storiesLocalDataSource.getAllStories()
        }, initialKey = null
    ).flow.map { pagingData ->
        pagingData.map { it.toStory() }
    }.flowOn(ioDispatcher)

    override fun getStoriesWithLocation(): Flow<Result<List<Story>>> = flow {
        val userPref = userPrefDataStore.userPref.first()
        val bearerToken = Constant.BEARER_TOKEN.plus(userPref.token)

        when (val source = storiesRemoteDataSource.getStoriesWithLocation(bearerToken)) {
            is ApiResult.ApiSuccess -> {
                val result = source.data.listStory.map { it.toStoryEntity().toStory() }
                emit(Result.Success(result))
            }
            is ApiResult.ApiError -> {
                emit(Result.Error(source.uiText))
            }
        }

    }.onStart { emit(Result.Loading) }.flowOn(ioDispatcher)

    override fun getStoriesFav(): Flow<Result<List<Story>>> = flow {
        try {
            storiesLocalDataSource.getStoriesFav().collect { stories ->
                emit(Result.Success(stories.map { it.toStory() }))
            }
        } catch (exception: Exception) {
            emit(Result.Error(UiText.StringResource(R.string.text_message_error)))
        }
    }.onStart { emit(Result.Loading) }.flowOn(ioDispatcher)


    override suspend fun setStoryFavorite(storyEntity: StoryEntity, isFavorite: Boolean) {
        withContext(ioDispatcher) {
            val data = storyEntity.copy(isFavorite = isFavorite)
            storiesLocalDataSource.updateStory(data)
        }
    }

    override fun addStory(
        description: String, photo: File, lat: Double?, lon: Double?
    ): Flow<Map<Boolean, UiText>> = flow {

        val userPref = userPrefDataStore.userPref.first()
        val bearerToken = Constant.BEARER_TOKEN.plus(userPref.token)

        val partMap: MutableMap<String, RequestBody> = mutableMapOf()

        partMap["description"] = description.createPartFromString()
        if (lat != null && lon != null) {
            partMap["lat"] = lat.createPartFromDouble()
            partMap["lon"] = lon.createPartFromDouble()
        }

        val requestPhoto = photo.createPartFromFile("image/jpg", "photo")

        val source = storiesRemoteDataSource.addStory(
            bearerToken, partMap, requestPhoto
        )

        when (source) {
            is ApiResult.ApiSuccess -> {
                val data = source.data
                val result = mapOf(!data.error to UiText.DynamicString(data.message))
                emit(result)
            }
            is ApiResult.ApiError -> {
                val result = mapOf(false to source.uiText)
                emit(result)
            }
        }
    }.flowOn(ioDispatcher)

}