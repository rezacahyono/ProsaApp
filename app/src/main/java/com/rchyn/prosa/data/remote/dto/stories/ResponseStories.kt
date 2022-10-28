package com.rchyn.prosa.data.remote.dto.stories

import com.google.gson.annotations.SerializedName

data class ResponseStories(

	@SerializedName("listStory")
	val listStory: List<StoryDto>,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("message")
	val message: String
)