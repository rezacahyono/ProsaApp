package com.rchyn.prosa.ui.fragments.register

import com.rchyn.prosa.utils.UiText

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val messageError: UiText? = null,
    val isSuccess: Boolean = false
)