package com.rchyn.prosa.ui.fragments.favorite

import androidx.lifecycle.*
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.domain.use_case.stories.GetStoriesFavoriteUseCase
import com.rchyn.prosa.domain.use_case.stories.SetStoryFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getStoriesFavoriteUseCase: GetStoriesFavoriteUseCase,
    private val setStoryFavoriteUseCase: SetStoryFavoriteUseCase
) : ViewModel() {


    val listStoryFav: LiveData<FavoriteUiState> = getStoriesFavoriteUseCase()
        .map {
            FavoriteUiState(listFavorite = it)
        }
        .onStart {
            emit(FavoriteUiState(isLoading = true))
        }.catch {
            emit(FavoriteUiState(isError = true))
        }
        .asLiveData()


    fun setStoryFavorite(story: Story) {
        viewModelScope.launch {
            setStoryFavoriteUseCase(story, !story.isFavorite)
        }
    }
}