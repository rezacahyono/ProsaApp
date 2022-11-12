package com.rchyn.prosa.utils

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val uiText: UiText) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
