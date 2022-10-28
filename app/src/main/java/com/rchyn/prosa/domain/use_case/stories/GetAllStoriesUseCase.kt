package com.rchyn.prosa.domain.use_case.stories

import androidx.paging.PagingData
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllStoriesUseCase @Inject constructor(
    private val storiesRepository: StoriesRepository
) {

    operator fun invoke(): Flow<PagingData<Story>> =
        storiesRepository.getAllStories()
}