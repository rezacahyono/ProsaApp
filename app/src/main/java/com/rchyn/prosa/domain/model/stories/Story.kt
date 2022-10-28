package com.rchyn.prosa.domain.model.stories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Story(
    val id: String,
    val photo: String,
    val name: String,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val date: String,
    val isFavorite: Boolean
) : Parcelable
