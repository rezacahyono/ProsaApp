package com.rchyn.prosa.ui.fragments.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.*
import com.rchyn.prosa.utils.Utilities.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val storiesFavoriteDummy =
        DataDummy.generateDummyStory().map { it.copy(isFavorite = true) }

    @Before
    fun setUp() {
        favoriteViewModel = FavoriteViewModel(storiesRepository)
    }

    @Test
    fun `When Get Stories Favorite Should Not Null, And Favorite Ui State Success List Story Not Empty`() =
        runTest {
            val expectedState = FavoriteUiState(listFavorite = storiesFavoriteDummy)

            val data = flow {
                emit(Result.Success(storiesFavoriteDummy))
            }

            whenever(storiesRepository.getStoriesFav()).thenReturn(data)

            advanceUntilIdle()
            val actualState = favoriteViewModel.storiesFavState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesFav()
            Assert.assertNotNull(actualState)
            Assert.assertFalse(actualState.isError)
            Assert.assertFalse(actualState.isLoading)
            Assert.assertTrue(actualState.listFavorite.isNotEmpty())
            Assert.assertEquals(
                expectedState.listFavorite.size,
                actualState.listFavorite.size
            )
        }

    @Test
    fun `When Get Stories Favorite Should Not Null, And Return Favorite Ui State List Story Empty`() =
        runTest {
            val storiesDummyEmpty: List<Story> = emptyList()
            val expectedState = FavoriteUiState(listFavorite = storiesDummyEmpty)
            val data = flow { emit(Result.Success(storiesDummyEmpty)) }

            whenever(storiesRepository.getStoriesFav()).thenReturn(data)

            advanceUntilIdle()
            val actualState = favoriteViewModel.storiesFavState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesFav()
            Assert.assertNotNull(actualState)
            Assert.assertFalse(actualState.isError)
            Assert.assertFalse(actualState.isLoading)
            Assert.assertTrue(actualState.listFavorite.isEmpty())
            Assert.assertEquals(
                expectedState.listFavorite.size,
                actualState.listFavorite.size
            )
        }


    @Test
    fun `When Get Stories Favorite Should Not Null, And Favorite Ui State Loading`() =
        runTest {
            val expectedState = FavoriteUiState(isLoading = true)

            val data = flow {
                emit(Result.Loading)
            }

            whenever(storiesRepository.getStoriesFav()).thenReturn(data)

            advanceUntilIdle()
            val actualState = favoriteViewModel.storiesFavState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesFav()
            Assert.assertNotNull(actualState)
            Assert.assertFalse(actualState.isError)
            Assert.assertTrue(actualState.isLoading)
            Assert.assertEquals(
                expectedState.isLoading,
                actualState.isLoading
            )
        }

    @Test
    fun `When Get Stories Favorite Should Not Null, And Favorite Ui State Error`() =
        runTest {
            val expectedState =
                FavoriteUiState(isError = true, messageError = UiText.DynamicString("Error"))

            val data = flow {
                emit(Result.Error(UiText.DynamicString("Error")))
            }

            whenever(storiesRepository.getStoriesFav()).thenReturn(data)

            advanceUntilIdle()
            val actualState = favoriteViewModel.storiesFavState.getOrAwaitValue()

            Mockito.verify(storiesRepository).getStoriesFav()
            Assert.assertNotNull(actualState)
            Assert.assertTrue(actualState.isError)
            Assert.assertFalse(actualState.isLoading)
            Assert.assertEquals(
                expectedState.isError,
                actualState.isError
            )
            Assert.assertEquals(
                (expectedState.messageError as UiText.DynamicString).value,
                (actualState.messageError as UiText.DynamicString).value
            )
        }

    @Test
    fun `When isFavorite Status False, Should call setStoryFavorite`() = runTest {
        val data = flow {
            emit(Result.Success(storiesFavoriteDummy))
        }

        val storyFavorite = storiesFavoriteDummy[0].copy(isFavorite = false)
        val isFavorite = storyFavorite.isFavorite

        whenever(
            storiesRepository.setStoryFavorite(
                storyFavorite.toStoryEntity(),
                !isFavorite
            )
        ).thenReturn(Unit)
        whenever(storiesRepository.getStoriesFav()).thenReturn(data)

        favoriteViewModel.setStoryFavorite(storyFavorite)

        advanceUntilIdle()
        favoriteViewModel.storiesFavState.getOrAwaitValue()

        Mockito.verify(storiesRepository)
            .setStoryFavorite(storyFavorite.toStoryEntity(), !isFavorite)
    }

    @Test
    fun `When isFavorite Status True, Should call setStoryFavorite`() = runTest {
        val data = flow {
            emit(Result.Success(storiesFavoriteDummy))
        }

        val storyFavorite = storiesFavoriteDummy[0].copy(isFavorite = true)
        val isFavorite = storyFavorite.isFavorite

        whenever(
            storiesRepository.setStoryFavorite(
                storyFavorite.toStoryEntity(),
                !isFavorite
            )
        ).thenReturn(Unit)
        whenever(storiesRepository.getStoriesFav()).thenReturn(data)

        favoriteViewModel.setStoryFavorite(storyFavorite)

        advanceUntilIdle()
        favoriteViewModel.storiesFavState.getOrAwaitValue()

        Mockito.verify(storiesRepository)
            .setStoryFavorite(storyFavorite.toStoryEntity(), !isFavorite)
    }

}















