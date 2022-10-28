package com.rchyn.prosa.ui.fragments.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.use_case.stories.GetAllStoriesUseCase
import com.rchyn.prosa.domain.use_case.stories.SetStoryFavoriteUseCase
import com.rchyn.prosa.domain.use_case.user.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authUserUseCase: AuthUserUseCase,
    getAllStoriesUseCase: GetAllStoriesUseCase,
    private val setStoryFavoriteUseCase: SetStoryFavoriteUseCase
) : ViewModel() {

    val userPref = authUserUseCase.userPref.asLiveData()

    val listStories: LiveData<HomeUiState> =
        getAllStoriesUseCase()
            .cachedIn(viewModelScope)
            .map { HomeUiState(listStory = it) }
            .onStart { emit(HomeUiState(isLoading = true)) }
            .catch { emit(HomeUiState(isError = true)) }
            .asLiveData()

    fun setStoryFavorite(story: Story) {
        viewModelScope.launch {
            setStoryFavoriteUseCase(story, !story.isFavorite)
        }
    }

}