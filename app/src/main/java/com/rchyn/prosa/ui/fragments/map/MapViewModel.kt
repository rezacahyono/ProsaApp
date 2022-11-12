package com.rchyn.prosa.ui.fragments.map

import android.util.Log
import androidx.lifecycle.*
import com.rchyn.prosa.domain.use_case.stories.GetStoriesWithLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rchyn.prosa.utils.Result

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getStoriesWithLocation: GetStoriesWithLocation
) : ViewModel() {

    private val _storiesWithLocationState: MutableLiveData<MapUiState> = MutableLiveData()
    val storiesWithLocationState: LiveData<MapUiState> = _storiesWithLocationState

    init {
        getAllStoryWithLocation()
    }

    private fun getAllStoryWithLocation() {
        viewModelScope.launch {
            getStoriesWithLocation().collect { result ->
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