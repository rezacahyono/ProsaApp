package com.rchyn.prosa.ui.fragments.map

import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.UiText


data class MapUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val messageError: UiText? = null,
    val listStory: List<Story> = emptyList()
)
