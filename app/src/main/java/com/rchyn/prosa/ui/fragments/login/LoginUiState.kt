package com.rchyn.prosa.ui.fragments.login

import com.rchyn.prosa.utils.UiText

data class LoginUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val messageError: UiText? = null,
    val isSuccess: Boolean = false
)