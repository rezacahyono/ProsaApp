package com.rchyn.prosa.data.repository.user

import com.rchyn.prosa.data.local.data_store.UserPrefDataStore
import com.rchyn.prosa.data.remote.data_source.auth.UserRemoteDataSource
import com.rchyn.prosa.model.user.User
import com.rchyn.prosa.utils.ApiResult
import com.rchyn.prosa.utils.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userPrefDataStore: UserPrefDataStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override val userPref: Flow<User>
        get() = userPrefDataStore.userPref.flowOn(ioDispatcher)

    override suspend fun setUserPref(user: User) {
        withContext(ioDispatcher) {
            userPrefDataStore.setUserPref(user)
        }
    }

    override fun login(email: String, password: String): Flow<Map<Boolean, UiText>> = flow {
        when (val source = userRemoteDataSource.login(email, password)) {
            is ApiResult.ApiSuccess -> {
                val data = source.data
                val result = mapOf(!data.error to UiText.DynamicString(data.message))
                emit(result)

                val user = data.loginResult.toUser()

                userPrefDataStore.setUserPref(user.copy(isLogin = true))
            }
            is ApiResult.ApiError -> {
                val result = mapOf(false to source.uiText)
                emit(result)
            }
        }
    }.flowOn(ioDispatcher)

    override fun register(
        name: String, email: String, password: String
    ): Flow<Map<Boolean, UiText>> = flow {
        when (val source = userRemoteDataSource.register(
            name, email, password
        )) {
            is ApiResult.ApiSuccess -> {
                val data = source.data
                val result = mapOf(!data.error to UiText.DynamicString(data.message))
                emit(result)
            }
            is ApiResult.ApiError -> {
                val result = mapOf(false to source.uiText)
                emit(result)
            }
        }
    }.flowOn(ioDispatcher)

}