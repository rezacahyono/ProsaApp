package com.rchyn.prosa.ui.fragments.favorite

import com.rchyn.prosa.domain.model.stories.Story

data class FavoriteUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val listFavorite: List<Story> = emptyList()
)
