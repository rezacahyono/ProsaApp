package com.rchyn.prosa.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class ResponsePlace(

	@SerializedName("features")
	val features: List<FeaturesItem>,

	@SerializedName("type")
	val type: String
)