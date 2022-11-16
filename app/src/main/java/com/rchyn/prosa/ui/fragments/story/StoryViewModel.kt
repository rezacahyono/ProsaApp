package com.rchyn.prosa.ui.fragments.story

import android.location.Location
import androidx.lifecycle.*
import com.rchyn.prosa.R
import com.rchyn.prosa.data.repository.place.PlaceRepository
import com.rchyn.prosa.data.repository.stories.StoriesRepository
import com.rchyn.prosa.ui.fragments.story.location.SearchLocationUiState
import com.rchyn.prosa.utils.Result
import com.rchyn.prosa.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storiesRepository: StoriesRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _myLocation: MutableLiveData<Location?> = MutableLiveData()
    val myLocation: LiveData<Location?> = _myLocation

    private val _place: MutableLiveData<SearchLocationUiState> = MutableLiveData()
    val place: LiveData<SearchLocationUiState> = _place

    fun addStory(
        description: String,
        photo: File,
    ): LiveData<StoryUIState> {
        return storiesRepository.addStory(
            description,
            photo,
            myLocation.value?.latitude,
            myLocation.value?.longitude
        )
            .map {
                StoryUIState(
                    isSuccess = it.keys.first(),
                    messageError = if (!it.keys.first()) it.values.first() else null,
                    isError = !it.keys.first()
                )
            }
            .onStart { emit(StoryUIState(isLoading = true)) }
            .catch {
                emit(
                    StoryUIState(
                        isError = true,
                        messageError = UiText.StringResource(R.string.text_message_error)
                    )
                )
            }.asLiveData()
    }

    fun getPlace(query: String) {
        viewModelScope.launch {
            placeRepository.getPlace(query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _place.value = SearchLocationUiState(listLocation = result.data)
                    }
                    is Result.Loading -> {
                        _place.value = SearchLocationUiState(isLoading = true)
                    }
                    is Result.Error -> {
                        _place.value =
                            SearchLocationUiState(isError = true, messageError = result.uiText)
                    }
                }
            }
        }
    }

    fun setMyLocation(location: Location) {
        _myLocation.value = location
    }

    fun clearMyLocation() {
        _myLocation.value = null
    }
}