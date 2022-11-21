package com.rchyn.prosa.ui.fragments.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.rchyn.prosa.adapter.ListStoryAdapter
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.DataDummy
import com.rchyn.prosa.utils.MainCoroutineRule
import com.rchyn.prosa.utils.Utilities.whenever
import com.rchyn.prosa.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var homeViewModel: HomeViewModel

    private val storiesDummy = DataDummy.generateDummyStory()
    private var userDummy = DataDummy.generateUserDummy()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(userRepository, storiesRepository)
    }


    @Test
    fun `When Get All Stories Should Not Null And Return Success`() = runTest {
        val data = StoriesPagingSource.snapshot(storiesDummy)
        val expectedStory = flow {
            emit(data)
        }

        whenever(storiesRepository.getAllStories()).thenReturn(expectedStory)

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        val actualStory = homeViewModel.allStories.getOrAwaitValue()

        differ.submitData(actualStory)
        Mockito.verify(storiesRepository).getAllStories()

        assertNotNull(differ.snapshot())
        assertEquals(storiesDummy, differ.snapshot())
        assertEquals(storiesDummy.size, differ.snapshot().size)
        assertEquals(storiesDummy[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `When isFavorite Status False, Should call setStoryFavorite`() = runTest {

        val storyFavorite = storiesDummy[0].copy(isFavorite = false)
        val isFavorite = storyFavorite.isFavorite

        whenever(
            storiesRepository.setStoryFavorite(
                storyFavorite.toStoryEntity(),
                !isFavorite
            )
        ).thenReturn(Unit)
        homeViewModel.setStoryFavorite(storyFavorite)

        Mockito.verify(storiesRepository)
            .setStoryFavorite(storyFavorite.toStoryEntity(), !isFavorite)
    }

    @Test
    fun `When isFavorite Status True, Should call setStoryFavorite`() = runTest {

        val storyFavorite = storiesDummy[0].copy(isFavorite = true)
        val isFavorite = storyFavorite.isFavorite

        whenever(
            storiesRepository.setStoryFavorite(
                storyFavorite.toStoryEntity(),
                !isFavorite
            )
        ).thenReturn(Unit)
        homeViewModel.setStoryFavorite(storyFavorite)

        Mockito.verify(storiesRepository)
            .setStoryFavorite(storyFavorite.toStoryEntity(), !isFavorite)
    }

    @Test
    fun `When Get User Pref Should Not Null and Return Success`() = runTest {
        val expectedUser = flow { emit(userDummy) }

        whenever(userRepository.userPref).thenReturn(expectedUser)

        val actualUser = homeViewModel.userPref.getOrAwaitValue()

        Mockito.verify(userRepository).userPref

        assertEquals(userDummy.name, actualUser.name)
        assertEquals(userDummy.token, actualUser.token)
        assertEquals(userDummy.isLogin, actualUser.isLogin)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


class StoriesPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}