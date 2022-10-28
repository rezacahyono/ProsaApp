package com.rchyn.prosa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val photo: String,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val date: String,
    val isFavorite: Boolean
)
