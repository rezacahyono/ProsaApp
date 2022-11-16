package com.rchyn.prosa.data.repository.user

import com.rchyn.prosa.model.user.User
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userPref: Flow<User>

    suspend fun setUserPref(user: User)

    fun login(email: String, password: String): Flow<Map<Boolean, UiText>>

    fun register(name: String, email: String, password: String): Flow<Map<Boolean, UiText>>
}