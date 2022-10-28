package com.rchyn.prosa.data.remote.dto.stories

import com.google.gson.annotations.SerializedName

data class StoryDto(

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("lon")
    val lon: Double? = null,

    @SerializedName("id")
    val id: String,

    @SerializedName("lat")
    val lat: Double? = null
)