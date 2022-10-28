package com.rchyn.prosa.domain.repository.stories

import androidx.paging.PagingData
import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoriesRepository {

    fun getAllStories(): Flow<PagingData<Story>>

    fun getStoriesFav(): Flow<List<Story>>

    suspend fun setStoryFavorite(storyEntity: StoryEntity, isFavorite: Boolean)

    fun addStory(
        description: String,
        photo: File,
        lat: Float?,
        lan: Float?
    ): Flow<Map<Boolean, UiText>>

}