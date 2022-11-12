package com.rchyn.prosa.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.use_case.stories.GetAllStoriesUseCase
import com.rchyn.prosa.domain.use_case.stories.SetStoryFavoriteUseCase
import com.rchyn.prosa.domain.use_case.user.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authUserUseCase: AuthUserUseCase,
    getAllStoriesUseCase: GetAllStoriesUseCase,
    private val setStoryFavoriteUseCase: SetStoryFavoriteUseCase
) : ViewModel() {

    val userPref = authUserUseCase.userPref.asLiveData()

    val storiesState: LiveData<PagingData<Story>> =
        getAllStoriesUseCase().cachedIn(viewModelScope).asLiveData()

    fun setStoryFavorite(story: Story) {
        viewModelScope.launch {
            setStoryFavoriteUseCase(story, !story.isFavorite)
        }
    }

}