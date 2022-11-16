package com.rchyn.prosa.data.repository.stories

import androidx.paging.PagingData
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow
import java.io.File
import com.rchyn.prosa.utils.Result

interface StoriesRepository {

    fun getAllStories(): Flow<PagingData<Story>>

    fun getStoriesWithLocation(): Flow<Result<List<Story>>>

    fun getStoriesFav(): Flow<Result<List<Story>>>

    suspend fun setStoryFavorite(storyEntity: StoryEntity, isFavorite: Boolean)

    fun addStory(
        description: String,
        photo: File,
        lat: Double?,
        lon: Double?
    ): Flow<Map<Boolean, UiText>>

}