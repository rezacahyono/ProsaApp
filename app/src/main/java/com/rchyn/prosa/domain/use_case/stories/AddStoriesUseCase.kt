package com.rchyn.prosa.domain.use_case.stories

import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class AddStoriesUseCase @Inject constructor(
    private val storiesRepository: StoriesRepository
) {

    operator fun invoke(
        description: String,
        photo: File,
        lat: Float? = null,
        lan: Float? = null
    ): Flow<Map<Boolean, UiText>> = storiesRepository.addStory(description, photo, lat, lan)
}