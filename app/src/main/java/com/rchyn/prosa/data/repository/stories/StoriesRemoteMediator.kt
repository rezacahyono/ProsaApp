package com.rchyn.prosa.data.repository.stories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rchyn.prosa.data.local.data_source.StoryLocalDataSource
import com.rchyn.prosa.data.local.data_store.UserPrefDataStore
import com.rchyn.prosa.data.local.entity.RemoteKeys
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.remote.data_source.stories.StoriesRemoteDataSource
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.utils.ApiResult
import com.rchyn.prosa.utils.Constant.BEARER_TOKEN
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val userPrefDataStore: UserPrefDataStore,
    private val storyLocalDataSource: StoryLocalDataSource,
    private val storyRemoteDataSource: StoriesRemoteDataSource,
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val userPref = userPrefDataStore.userPref.first()
            val bearerToken = BEARER_TOKEN.plus(userPref.token)

            val storyDataSource = when (val source = storyRemoteDataSource.getAllStories(
                bearerToken,
                page,
                state.config.pageSize
            )) {
                is ApiResult.ApiSuccess -> source.data
                is ApiResult.ApiError -> return MediatorResult.Error(source.exception!!)
            }

            val endOfPaginationReached = storyDataSource.listStory.isEmpty()

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = storyDataSource.listStory.map {
                RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
            }

            val stories = storyDataSource.listStory.map {
                val isFavorite = storyLocalDataSource.isNewFavorite(it.id)
                it.toStoryEntity().copy(isFavorite = isFavorite)
            }


            if (loadType == LoadType.REFRESH) {
                storyLocalDataSource.deleteRemoteKeys()
                storyLocalDataSource.deleteStories()
            }

            storyLocalDataSource.insertRemoteKeys(keys)
            storyLocalDataSource.insertStories(stories)

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyLocalDataSource.getRemoteKeyById(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyLocalDataSource.getRemoteKeyById(data.id)
        }
    }

    private companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }

}