package com.rchyn.prosa.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rchyn.prosa.R
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class ApiResult<T : Any> {
    class ApiSuccess<T : Any>(val data: T) : ApiResult<T>()
    class ApiError<T : Any>(val uiText: UiText, val exception: Exception? = null) : ApiResult<T>()
}


suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.ApiSuccess(body)
        } else {
            val errorBody = response.errorBody()
            val messageError =
                Gson().fromJson(errorBody!!.string(), JsonObject::class.java)["message"].asString
            ApiResult.ApiError(UiText.DynamicString(messageError))
        }
    } catch (e: HttpException) {
        ApiResult.ApiError(UiText.StringResource(R.string.text_message_error_server), e)
    } catch (e: IOException) {
        ApiResult.ApiError(
            UiText.StringResource(R.string.text_message_error_internet_connection), e
        )
    }
}

