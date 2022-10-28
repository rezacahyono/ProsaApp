package com.rchyn.prosa.domain.use_case.user

import com.rchyn.prosa.domain.model.user.User
import com.rchyn.prosa.domain.repository.user.UserRepository
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    val userPref = userRepository.userPref

    suspend fun setUserPref(user: User) = userRepository.setUserPref(user)

    operator fun invoke(email: String, password: String): Flow<Map<Boolean, UiText>> =
        userRepository.login(email, password)

}