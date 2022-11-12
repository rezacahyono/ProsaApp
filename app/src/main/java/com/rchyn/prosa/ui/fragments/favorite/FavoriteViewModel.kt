package com.rchyn.prosa.ui.fragments.favorite

import androidx.lifecycle.*
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.use_case.stories.GetStoriesFavoriteUseCase
import com.rchyn.prosa.domain.use_case.stories.SetStoryFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rchyn.prosa.utils.Result

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getStoriesFavoriteUseCase: GetStoriesFavoriteUseCase,
    private val setStoryFavoriteUseCase: SetStoryFavoriteUseCase
) : ViewModel() {

    private val _storiesFavState: MutableLiveData<FavoriteUiState> = MutableLiveData()
    val storiesFavState: LiveData<FavoriteUiState> = _storiesFavState

    init {
        getStoriesFav()
    }

    private fun getStoriesFav() {
        viewModelScope.launch {
            getStoriesFavoriteUseCase().collect { result ->
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
            setStoryFavoriteUseCase(story, !story.isFavorite)
        }
    }
}