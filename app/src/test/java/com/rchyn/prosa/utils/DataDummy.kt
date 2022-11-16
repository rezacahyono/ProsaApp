package com.rchyn.prosa.utils

import com.rchyn.prosa.model.place.Place
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.model.user.User
import java.io.File

object DataDummy {

    fun generateDummyStory(): List<Story> {
        val list: MutableList<Story> = mutableListOf()

        for (i in 0 until 10) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photo = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                name = "Dimas",
                description = "Lorem Ipsum",
                lat = -10.212,
                lon = -16.002,
                date = "2022-01-08T06:34:18.598Z",
                isFavorite = false
            )
            list.add(story)
        }
        return list
    }

    fun generateAddDummyStory() = AddStory("Description Test", File("Path"), 0.0, 0.0)

    fun generateUserDummy() = User(
        "Dimas",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I",
        true
    )

    fun generateDummyPlace(): List<Place> {
        val list: MutableList<Place> = mutableListOf()
        for (i in 0 until 10) {
            val place = Place(
                name = "Kreo Selatan",
                city = "Tangerang",
                state = "Banten",
                county = "Larangan",
                country = "Indonesia",
                village = "Kreo Selatan",
                formatted = "Kreo Selatan, Tangerang 15155, Banten, Indonesia",
                lat = 0.0,
                lon = -0.0
            )
            list.add(place)
        }
        return list
    }

}

data class AddStory(
    val description: String, val photo: File, val lat: Double, val lon: Double
)