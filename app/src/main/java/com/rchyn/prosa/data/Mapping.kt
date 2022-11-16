package com.rchyn.prosa.data

import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.remote.dto.place.FeaturesItem
import com.rchyn.prosa.data.remote.dto.stories.StoryDto
import com.rchyn.prosa.model.place.Place
import com.rchyn.prosa.model.stories.Story

fun StoryDto.toStoryEntity() =
    StoryEntity(
        id = id,
        name = name,
        photo = photoUrl,
        description = description,
        lat = lat,
        lon = lon,
        date = createdAt,
        isFavorite = false
    )

fun StoryEntity.toStory() =
    Story(
        id = id,
        name = name,
        photo = photo,
        description = description,
        lat = lat,
        lon = lon,
        date = date,
        isFavorite = isFavorite
    )

fun Story.toStoryEntity() =
    StoryEntity(
        id = id,
        name = name,
        photo = photo,
        description = description,
        lat = lat,
        lon = lon,
        date = date,
        isFavorite = false
    )

fun FeaturesItem.toPlace() =
    Place(
        name = this.properties.name ?: "",
        city = this.properties.city ?: "",
        state = this.properties.state ?: "",
        county = this.properties.county ?: "",
        country = this.properties.country ?: "",
        village = this.properties.village ?: "",
        formatted = this.properties.formatted ?: "",
        lat = this.properties.lat ?: 0.0,
        lon = this.properties.lon ?: 0.0
    )