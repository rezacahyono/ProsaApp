package com.rchyn.prosa.ui.fragments.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.*
import com.rchyn.prosa.utils.Utilities.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class MapViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    private lateinit var mapViewModel: MapViewModel
    private val storiesDummy = DataDummy.generateDummyStory()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(storiesRepository)
    }


    @Test
    fun `When Get All Story With Location Should Not Null, And Return Map Ui State Success Story Not Empty`() =
        runTest {
            val expectedState = MapUiState(listStory = storiesDummy)
            val data = flow { emit(Result.Success(storiesDummy)) }

            whenever(storiesRepository.getStoriesWithLocation()).thenReturn(data)

            advanceUntilIdle()
            val actualState = mapViewModel.storiesWithLocationState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesWithLocation()
            assertNotNull(actualState)
            assertFalse(actualState.isError)
            assertFalse(actualState.isLoading)
            assertTrue(actualState.listStory.isNotEmpty())
            assertEquals(
                expectedState.listStory.size,
                actualState.listStory.size
            )
        }

    @Test
    fun `When Get All Story With Location Should Not Null, And Return Map Ui State Success Story Empty`() =
        runTest {
            val dataDummy: List<Story> = emptyList()
            val expectedState = MapUiState(listStory = dataDummy)
            val data = flow { emit(Result.Success(dataDummy)) }

            whenever(storiesRepository.getStoriesWithLocation()).thenReturn(data)

            advanceUntilIdle()
            val actualState = mapViewModel.storiesWithLocationState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesWithLocation()
            assertNotNull(actualState)
            assertFalse(actualState.isError)
            assertFalse(actualState.isLoading)
            assertTrue(actualState.listStory.isEmpty())
            assertEquals(
                expectedState.listStory.size,
                actualState.listStory.size
            )
        }

    @Test
    fun `When Get All Story With Location Should Not Null, And Return Map Ui State Loading`() =
        runTest {
            val expectedState = MapUiState(isLoading = true)
            val data = flow { emit(Result.Loading) }

            whenever(storiesRepository.getStoriesWithLocation()).thenReturn(data)

            advanceUntilIdle()
            val actualState = mapViewModel.storiesWithLocationState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesWithLocation()
            assertNotNull(actualState)
            assertFalse(actualState.isError)
            assertTrue(actualState.isLoading)
            assertFalse(actualState.listStory.isNotEmpty())
            assertEquals(
                expectedState.listStory.size,
                actualState.listStory.size
            )
        }

    @Test
    fun `When Get All Story With Location Should Not Null, And Return Map Ui State Error`() =
        runTest {
            val expectedState =
                MapUiState(isError = true, messageError = UiText.DynamicString("Error"))
            val data = flow { emit(Result.Error(UiText.DynamicString("Error"))) }

            whenever(storiesRepository.getStoriesWithLocation()).thenReturn(data)

            advanceUntilIdle()
            val actualState = mapViewModel.storiesWithLocationState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesWithLocation()
            assertNotNull(actualState)
            assertTrue(actualState.isError)
            assertFalse(actualState.isLoading)
            assertFalse(actualState.listStory.isNotEmpty())
            assertEquals(
                expectedState.listStory.size,
                actualState.listStory.size
            )
            assertEquals(
                (expectedState.messageError as UiText.DynamicString).value,
                (actualState.messageError as UiText.DynamicString).value
            )
        }
}