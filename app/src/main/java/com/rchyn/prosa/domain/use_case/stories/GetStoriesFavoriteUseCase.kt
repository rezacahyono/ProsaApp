package com.rchyn.prosa.domain.use_case.stories

import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesFavoriteUseCase @Inject constructor(
    private val storiesRepository: StoriesRepository
) {

    operator fun invoke(): Flow<List<Story>> = storiesRepository.getStoriesFav()

}