package com.rchyn.prosa.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.rchyn.prosa.domain.model.user.User

data class LoginResult(

    @SerializedName("name")
    val name: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("token")
    val token: String
) {
    fun toUser() =
        User(
            name = name,
            token = token
        )
}