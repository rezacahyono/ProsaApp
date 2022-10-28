package com.rchyn.prosa.domain.model.user

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val name: String = "",
    val token: String = "",
    val isLogin:Boolean = false
)
