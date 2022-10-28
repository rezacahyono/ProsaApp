package com.rchyn.prosa.domain.use_case.user

import com.rchyn.prosa.domain.repository.user.UserRepository
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<Map<Boolean, UiText>> =
        userRepository.register(name, email, password)

}