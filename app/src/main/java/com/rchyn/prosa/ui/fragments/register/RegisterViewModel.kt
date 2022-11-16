package com.rchyn.prosa.ui.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rchyn.prosa.R
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun register(
        name: String, email: String, password: String
    ): LiveData<RegisterUiState> = userRepository.register(name, email, password)
        .map {
            RegisterUiState(
                isSuccess = it.keys.first(),
                messageError = if (!it.keys.first()) it.values.first() else null,
                isError = !it.keys.first()
            )
        }
        .onStart { emit(RegisterUiState(isLoading = true)) }
        .catch {
            emit(
                RegisterUiState(
                    isError = true,
                    messageError = UiText.StringResource(R.string.text_message_error)
                )
            )
        }
        .asLiveData()

}