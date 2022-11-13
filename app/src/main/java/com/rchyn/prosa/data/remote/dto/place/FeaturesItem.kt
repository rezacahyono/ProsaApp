package com.rchyn.prosa.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class FeaturesItem(

	@SerializedName("properties")
	val properties: Properties
)