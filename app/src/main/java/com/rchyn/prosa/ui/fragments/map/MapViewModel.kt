package com.rchyn.prosa.ui.fragments.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storiesRepository: StoriesRepository
) : ViewModel() {

    private val _storiesWithLocationState: MutableLiveData<MapUiState> = MutableLiveData()
    val storiesWithLocationState: LiveData<MapUiState> = _storiesWithLocationState

    init {
        getAllStoryWithLocation()
    }

    private fun getAllStoryWithLocation() {
        viewModelScope.launch {
            storiesRepository.getStoriesWithLocation().collect { result ->
                when (result) {
                    is Result.Success -> _storiesWithLocationState.value =
                        MapUiState(listStory = result.data)
                    is Result.Loading -> _storiesWithLocationState.value =
                        MapUiState(isLoading = true)
                    is Result.Error -> _storiesWithLocationState.value =
                        MapUiState(
                            isError = true,
                            messageError = result.uiText
                        )
                }
            }
        }
    }

}