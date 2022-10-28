package com.rchyn.prosa.data.remote.data_source.auth

import com.rchyn.prosa.data.remote.dto.user.ResponseUser
import com.rchyn.prosa.data.remote.retrofit.StoryApi
import com.rchyn.prosa.utils.ApiResult
import com.rchyn.prosa.utils.handleApi
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val storyApi: StoryApi
) {

    suspend fun login(email: String, password: String): ApiResult<ResponseUser> =
        handleApi { storyApi.login(email, password) }


    suspend fun register(name: String, email: String, password: String): ApiResult<ResponseUser> =
        handleApi { storyApi.register(name, email, password) }
}