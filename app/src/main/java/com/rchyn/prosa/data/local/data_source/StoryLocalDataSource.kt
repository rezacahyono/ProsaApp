package com.rchyn.prosa.data.local.data_source

import androidx.paging.PagingSource
import com.rchyn.prosa.data.local.entity.RemoteKeys
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.local.room.RemoteKeysDao
import com.rchyn.prosa.data.local.room.StoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryLocalDataSource @Inject constructor(
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao
) {
    fun getAllStories(): PagingSource<Int, StoryEntity> = storyDao.getAllStories()

    fun getStoriesFav(): Flow<List<StoryEntity>> = storyDao.getStoriesFav()

    suspend fun insertStories(stories: List<StoryEntity>) =
        storyDao.insertStories(stories)

    suspend fun updateStory(storyEntity: StoryEntity) =
        storyDao.updateStory(storyEntity)

    suspend fun deleteStories() =
        storyDao.deleteStories()

    suspend fun isNewFavorite(id: String) =
        storyDao.isNewFavorite(id)

    suspend fun getRemoteKeyById(id: String) =
        remoteKeysDao.getRemoteKeysById(id)

    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeys>) =
        remoteKeysDao.insertAll(remoteKeys)

    suspend fun deleteRemoteKeys() =
        remoteKeysDao.deleteRemoteKeys()
}