package com.rchyn.prosa.ui.fragments.story

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rchyn.prosa.data.repository.place.PlaceRepository
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.ui.fragments.story.location.SearchLocationUiState
import com.rchyn.prosa.utils.*
import com.rchyn.prosa.utils.Utilities.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var placeRepository: PlaceRepository
    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var location: Location

    private val storyDummy = DataDummy.generateAddDummyStory()
    private val placesDummy = DataDummy.generateDummyPlace()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storiesRepository, placeRepository)
    }

    @Test
    fun `When Get Place Should Not Null, And Return Search Location Ui State Success`() = runTest {
        val expectedState = SearchLocationUiState(listLocation = placesDummy)
        val data = flow {
            emit(Result.Success(placesDummy))
        }

        whenever(placeRepository.getPlace("Kreo")).thenReturn(data)

        storyViewModel.getPlace("Kreo")
        val actualState = storyViewModel.place.getOrAwaitValue()

        Mockito.verify(placeRepository).getPlace("Kreo")
        assertNotNull(actualState)
        assertTrue(actualState.listLocation.isNotEmpty())
        assertFalse(actualState.isLoading)
        assertFalse(actualState.isError)
        assertEquals(expectedState.listLocation.size, actualState.listLocation.size)
        assertEquals(expectedState.listLocation[0].name, actualState.listLocation[0].name)
    }

    @Test
    fun `When Get Place Should Not Null, And Return Search Location Ui State Loading`() = runTest {
        val expectedState = SearchLocationUiState(isLoading = true)
        val data = flow {
            emit(Result.Loading)
        }

        whenever(placeRepository.getPlace("Kreo")).thenReturn(data)

        storyViewModel.getPlace("Kreo")
        val actualState = storyViewModel.place.getOrAwaitValue()

        Mockito.verify(placeRepository).getPlace("Kreo")
        assertNotNull(actualState)
        assertTrue(actualState.isLoading)
        assertFalse(actualState.isError)
        assertFalse(actualState.listLocation.isNotEmpty())
        assertEquals(expectedState.isLoading, actualState.isLoading)
    }

    @Test
    fun `When Get Place Should Not Null, And Return Search Location Ui State Error`() = runTest {
        val expectedState =
            SearchLocationUiState(isError = true, messageError = UiText.DynamicString("Error"))
        val data = flow {
            emit(Result.Error(UiText.DynamicString("Error")))
        }

        whenever(placeRepository.getPlace("Kreo")).thenReturn(data)

        storyViewModel.getPlace("Kreo")
        val actualState = storyViewModel.place.getOrAwaitValue()

        Mockito.verify(placeRepository).getPlace("Kreo")
        assertNotNull(actualState)
        assertTrue(actualState.isError)
        assertFalse(actualState.isLoading)
        assertFalse(actualState.listLocation.isNotEmpty())
        assertEquals(expectedState.isError, actualState.isError)
        assertEquals(
            (expectedState.messageError as UiText.DynamicString).value,
            (actualState.messageError as UiText.DynamicString).value
        )
    }

    @Test
    fun `When Add Story Should Not Null, And Return Story Ui State Success`() = runTest {
        location.apply {
            latitude = 0.0
            longitude = 0.0
        }

        val dummy = storyDummy.copy(lat = location.latitude, lon = location.longitude)

        val expectedState = StoryUIState(isSuccess = true)

        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(
            storiesRepository.addStory(
                dummy.description,
                dummy.photo,
                dummy.lat,
                dummy.lon
            )
        ).thenReturn(data)

        storyViewModel.setMyLocation(location)
        var actualState = StoryUIState()
        storyViewModel.addStory(dummy.description, dummy.photo).observeForever { state ->
            actualState = state
        }

        Mockito.verify(storiesRepository).addStory(
            dummy.description,
            dummy.photo,
            dummy.lat,
            dummy.lon
        )
        assertNotNull(actualState)
        assertTrue(actualState.isSuccess)
        assertFalse(actualState.isError)
        assertFalse(actualState.isLoading)
        assertEquals(expectedState.isSuccess, actualState.isSuccess)
    }

    @Test
    fun `When Add Story Should Not Null, And Return Story Ui State Loading`() = runTest {
        location.apply {
            latitude = 0.0
            longitude = 0.0
        }

        val dummy = storyDummy.copy(lat = location.latitude, lon = location.longitude)

        val expectedState = StoryUIState(isLoading = true)

        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(
            storiesRepository.addStory(
                dummy.description,
                dummy.photo,
                dummy.lat,
                dummy.lon
            )
        ).thenReturn(data)

        storyViewModel.setMyLocation(location)
        val actualState = storyViewModel.addStory(dummy.description, dummy.photo).getOrAwaitValue()

        Mockito.verify(storiesRepository).addStory(
            dummy.description,
            dummy.photo,
            dummy.lat,
            dummy.lon
        )
        assertNotNull(actualState)
        assertTrue(actualState.isLoading)
        assertFalse(actualState.isError)
        assertFalse(actualState.isSuccess)
        assertEquals(expectedState.isLoading, actualState.isLoading)
    }

    @Test
    fun `When Add Story Should Not Null, And Return Story Ui State Error`() = runTest {
        location.apply {
            latitude = 0.0
            longitude = 0.0
        }

        val dummy = storyDummy.copy(lat = location.latitude, lon = location.longitude)

        val expectedState =
            StoryUIState(isError = true, messageError = UiText.DynamicString("Error"))

        val data = flow {
            emit(mapOf(false to UiText.DynamicString("Error")))
        }

        whenever(
            storiesRepository.addStory(
                dummy.description,
                dummy.photo,
                dummy.lat,
                dummy.lon
            )
        ).thenReturn(data)

        storyViewModel.setMyLocation(location)
        var actualState = StoryUIState()
        storyViewModel.addStory(dummy.description, dummy.photo).observeForever { state ->
            actualState = state
        }

        Mockito.verify(storiesRepository).addStory(
            dummy.description,
            dummy.photo,
            dummy.lat,
            dummy.lon
        )
        assertNotNull(actualState)
        assertTrue(actualState.isError)
        assertFalse(actualState.isSuccess)
        assertFalse(actualState.isLoading)
        assertEquals(expectedState.isError, actualState.isError)
        assertEquals(
            (expectedState.messageError as UiText.DynamicString).value,
            (actualState.messageError as UiText.DynamicString).value
        )
    }
}