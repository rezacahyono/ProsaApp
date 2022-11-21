package com.rchyn.prosa.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.model.stories.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storiesRepository: StoriesRepository,
) : ViewModel() {

    val userPref by lazy { userRepository.userPref.asLiveData() }

    val allStories: LiveData<PagingData<Story>> by lazy {
        storiesRepository.getAllStories().cachedIn(viewModelScope).asLiveData()
    }

    fun setStoryFavorite(story: Story) {
        viewModelScope.launch {
            val storyEntity = story.toStoryEntity()
            storiesRepository.setStoryFavorite(storyEntity, !story.isFavorite)
        }
    }

}