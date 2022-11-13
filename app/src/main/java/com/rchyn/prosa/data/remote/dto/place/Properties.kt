package com.rchyn.prosa.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class Properties(

	@SerializedName("country")
	val country: String? = null,

	@SerializedName("result_type")
	val resultType: String? = null,

	@SerializedName("city")
	val city: String? = null,

	@SerializedName("formatted")
	val formatted: String? = null,

	@SerializedName("lon")
	val lon: Double? = 0.0,

	@SerializedName("country_code")
	val countryCode: String? = null,

	@SerializedName("address_line2")
	val addressLine2: String? = null,

	@SerializedName("address_line1")
	val addressLine1: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("state")
	val state: String? = null,

	@SerializedName("category")
	val category: String? = null,

	@SerializedName("lat")
	val lat: Double? = 0.0,

	@SerializedName("place_id")
	val placeId: String? = null,

	@SerializedName("postcode")
	val postcode: String? = null,

	@SerializedName("district")
	val district: String? = null,

	@SerializedName("suburb")
	val suburb: String? = null,

	@SerializedName("county")
	val county: String? = null,

	@SerializedName("village")
	val village: String? = null
)