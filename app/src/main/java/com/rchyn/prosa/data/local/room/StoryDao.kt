package com.rchyn.prosa.data.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.rchyn.prosa.data.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    @Query("SELECT storyentity.*  FROM storyentity INNER JOIN remotekeys ON storyentity.id = remotekeys.id ORDER BY remotekeys.nextKey ASC")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM storyentity WHERE isFavorite = 1 ORDER BY date ASC")
    fun getStoriesFav(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Update
    suspend fun updateStory(storyEntity: StoryEntity)

    @Query("DELETE FROM storyentity WHERE isFavorite = 0")
    suspend fun deleteStories()

    @Query("SELECT EXISTS(SELECT * FROM storyentity WHERE id = :id AND isFavorite = 1)")
    suspend fun isNewFavorite(id: String): Boolean
}