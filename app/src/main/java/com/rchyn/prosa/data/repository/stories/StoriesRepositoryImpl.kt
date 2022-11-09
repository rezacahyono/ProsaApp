package com.rchyn.prosa.data.repository.stories

import androidx.paging.*
import com.rchyn.prosa.data.local.data_source.StoryLocalDataSource
import com.rchyn.prosa.data.local.data_store.UserPrefDataStore
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.remote.data_source.stories.StoriesRemoteDataSource
import com.rchyn.prosa.data.toStory
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import com.rchyn.prosa.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val userPrefDataStore: UserPrefDataStore,
    private val storiesLocalDataSource: StoryLocalDataSource,
    private val storiesRemoteDataSource: StoriesRemoteDataSource
) : StoriesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(): Flow<PagingData<Story>> = Pager(
        config = PagingConfig(pageSize = 2),
        remoteMediator = StoriesRemoteMediator(
            userPrefDataStore,
            storiesLocalDataSource,
            storiesRemoteDataSource
        ),
        pagingSourceFactory = {
            storiesLocalDataSource.getAllStories()
        },
        initialKey = null
    ).flow.map { pagingData ->
        pagingData.map { it.toStory() }
    }.flowOn(Dispatchers.IO)

    override fun getStoriesFav(): Flow<List<Story>> =
        storiesLocalDataSource.getStoriesFav().map { stories ->
            stories.map { it.toStory() }
        }.flowOn(Dispatchers.IO)


    override suspend fun setStoryFavorite(storyEntity: StoryEntity, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            val data = storyEntity.copy(isFavorite = isFavorite)
            storiesLocalDataSource.updateStory(data)
        }
    }

    override fun addStory(
        description: String,
        photo: File,
        lat: Float?,
        lan: Float?
    ): Flow<Map<Boolean, UiText>> = flow {

        val userPref = userPrefDataStore.userPref.first()
        val bearerToken = Constant.BEARER_TOKEN.plus(userPref.token)

        val partMap: MutableMap<String, RequestBody> = mutableMapOf()

        partMap["description"] = description.createPartFromString()
        if (lat != null && lan != null) {
            partMap["lat"] = lat.createPartFromFloat()
            partMap["lan"] = lan.createPartFromFloat()
        }

        val requestPhoto = photo.createPartFromFile("image/jpg", "photo")

        val source = storiesRemoteDataSource.addStory(
            bearerToken,
            partMap,
            requestPhoto
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
    }.flowOn(Dispatchers.IO)

}