package com.rchyn.prosa.ui.fragments.home

import androidx.paging.PagingData
import com.rchyn.prosa.domain.model.stories.Story

data class HomeUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val listStory: PagingData<Story>? = null
)
