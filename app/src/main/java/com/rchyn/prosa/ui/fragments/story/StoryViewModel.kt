package com.rchyn.prosa.ui.fragments.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rchyn.prosa.R
import com.rchyn.prosa.domain.use_case.stories.AddStoriesUseCase
import com.rchyn.prosa.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val addStoriesUseCase: AddStoriesUseCase
) : ViewModel() {

    fun addStory(
        description: String,
        photo: File
    ): LiveData<StoryUIState> {
        return addStoriesUseCase(description, photo)
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
}