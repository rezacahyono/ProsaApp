package com.rchyn.prosa.ui.fragments.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rchyn.prosa.data.toStoryEntity
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val storiesRepository: StoriesRepository
) : ViewModel() {

    private val _storiesFavState: MutableLiveData<FavoriteUiState> = MutableLiveData()
    val storiesFavState: LiveData<FavoriteUiState> = _storiesFavState

    init {
        getStoriesFav()
    }

    private fun getStoriesFav() {
        viewModelScope.launch {
            storiesRepository.getStoriesFav().collect { result ->
                when (result) {
                    is Result.Success -> _storiesFavState.value =
                        FavoriteUiState(listFavorite = result.data)
                    is Result.Loading -> _storiesFavState.value = FavoriteUiState(isLoading = true)
                    is Result.Error -> _storiesFavState.value =
                        FavoriteUiState(
                            isError = true,
                            messageError = result.uiText
                        )
                }
            }
        }
    }

    fun setStoryFavorite(story: Story) {
        viewModelScope.launch {
            val storyEntity = story.toStoryEntity()
            storiesRepository.setStoryFavorite(storyEntity, !story.isFavorite)
        }
    }
}