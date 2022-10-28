package com.rchyn.prosa.domain.use_case.stories

import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import javax.inject.Inject

class SetStoryFavoriteUseCase @Inject constructor(
    private val storiesRepository: StoriesRepository
) {

    suspend operator fun invoke(story: Story, isFavorite: Boolean) {
        val storyEntity = story.toStoryEntity()
        storiesRepository.setStoryFavorite(storyEntity, isFavorite)
    }
}