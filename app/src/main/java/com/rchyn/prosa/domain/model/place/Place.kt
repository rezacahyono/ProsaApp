package com.rchyn.prosa.domain.model.place

data class Place(
    val name: String,
    val city: String,
    val state: String,
    val county: String,
    val country: String,
    val village: String,
    val formatted: String,
    val lat: Double,
    val lon: Double
)