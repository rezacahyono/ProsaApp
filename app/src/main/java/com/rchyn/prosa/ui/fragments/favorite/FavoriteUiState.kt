package com.rchyn.prosa.ui.fragments.favorite

import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.UiText

data class FavoriteUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val messageError: UiText? = null,
    val listFavorite: List<Story> = emptyList()
)
