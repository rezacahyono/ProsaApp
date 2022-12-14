package com.rchyn.prosa.data.remote.data_source.stories

import com.rchyn.prosa.data.remote.dto.stories.ResponseStories
import com.rchyn.prosa.data.remote.retrofit.StoryApi
import com.rchyn.prosa.utils.ApiResult
import com.rchyn.prosa.utils.handleApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoriesRemoteDataSource @Inject constructor(
    private val storyApi: StoryApi
) {

    suspend fun getAllStories(
        bearerToken: String,
        page: Int,
        size: Int
    ) = handleApi {
        storyApi.getAllStories(
            bearerToken = bearerToken,
            page = page,
            size = size
        )
    }

    suspend fun getStoriesWithLocation(bearerToken: String, location: Int = 1) =
        handleApi { storyApi.getAllStories(bearerToken = bearerToken, location = location) }


    suspend fun addStory(
        bearerToken: String,
        partMap: MutableMap<String, RequestBody>,
        photo: MultipartBody.Part
    ): ApiResult<ResponseStories> = handleApi { storyApi.addStory(bearerToken, partMap, photo) }
}