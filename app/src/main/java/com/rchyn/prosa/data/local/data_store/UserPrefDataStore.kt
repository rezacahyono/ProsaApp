package com.rchyn.prosa.data.local.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import com.rchyn.prosa.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPrefDataStore @Inject constructor(
    private val userPrefDataStore: DataStore<User>
) {
    val userPref: Flow<User> = userPrefDataStore.data

    suspend fun setUserPref(user: User) {
        userPrefDataStore.updateData {
            it.copy(
                name = user.name,
                token = user.token,
                isLogin = user.isLogin
            )
        }
    }

    companion object UserPrefSerializer : Serializer<User> {
        override val defaultValue: User
            get() = User()

        override suspend fun readFrom(input: InputStream): User =
            withContext(Dispatchers.IO) {
                try {
                    Json.decodeFromString(
                        deserializer = User.serializer(),
                        string = input.readBytes().decodeToString()
                    )
                } catch (e: SerializationException) {
                    e.printStackTrace()
                    defaultValue
                }
            }


        override suspend fun writeTo(t: User, output: OutputStream) {
            withContext(Dispatchers.IO) {
                output.write(
                    Json.encodeToString(
                        serializer = User.serializer(), value = t
                    ).encodeToByteArray()
                )
            }
        }
    }

}