package com.rchyn.prosa.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rchyn.prosa.R
import com.rchyn.prosa.model.user.User
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userPref: LiveData<Boolean> by lazy {
        userRepository.userPref.map { it.isLogin }.asLiveData()
    }

    fun login(email: String, password: String): LiveData<LoginUiState> {
        return userRepository.login(email, password).map {
                LoginUiState(
                    isSuccess = it.keys.first(),
                    messageError = if (!it.keys.first()) it.values.first() else null,
                    isError = !it.keys.first()
                )
            }.onStart { emit(LoginUiState(isLoading = true)) }.catch {
                emit(
                    LoginUiState(
                        isError = true,
                        messageError = UiText.StringResource(R.string.text_message_error)
                    )
                )
            }.asLiveData()
    }

    fun setUserPref(user: User) {
        viewModelScope.launch {
            userRepository.setUserPref(user)
        }
    }
}