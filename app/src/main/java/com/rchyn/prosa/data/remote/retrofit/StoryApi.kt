package com.rchyn.prosa.data.remote.retrofit

import com.rchyn.prosa.data.remote.dto.stories.ResponseStories
import com.rchyn.prosa.data.remote.dto.user.ResponseUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseUser>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponseUser>


    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponseStories

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") bearerToken: String,
        @PartMap partMap: MutableMap<String, RequestBody>,
        @Part photo: MultipartBody.Part,
    ): Response<ResponseStories>


}