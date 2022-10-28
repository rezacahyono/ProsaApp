package com.rchyn.prosa.data

import com.rchyn.prosa.data.local.entity.StoryEntity
import com.rchyn.prosa.data.remote.dto.stories.StoryDto
import com.rchyn.prosa.domain.model.stories.Story

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