package com.rchyn.prosa.ui.fragments.story

import com.rchyn.prosa.utils.UiText

data class StoryUIState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val messageError: UiText? = null,
)
