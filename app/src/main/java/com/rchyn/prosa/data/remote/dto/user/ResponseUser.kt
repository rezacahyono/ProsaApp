package com.rchyn.prosa.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class ResponseUser(

	@SerializedName("loginResult")
	val loginResult: LoginResult,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("message")
	val message: String
)