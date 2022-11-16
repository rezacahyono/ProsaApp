package com.rchyn.prosa.ui.fragments.story.location

import com.rchyn.prosa.model.place.Place
import com.rchyn.prosa.utils.UiText

data class SearchLocationUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val messageError: UiText? = null,
    val listLocation: List<Place> = emptyList()
)
