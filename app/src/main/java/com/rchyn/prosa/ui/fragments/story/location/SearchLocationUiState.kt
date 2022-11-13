package com.rchyn.prosa.ui.fragments.story.location

import com.rchyn.prosa.domain.model.place.Place

data class SearchLocationUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val listLocation: List<Place> = emptyList()
)
