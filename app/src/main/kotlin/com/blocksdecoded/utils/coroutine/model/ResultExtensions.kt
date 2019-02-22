package com.blocksdecoded.utils.coroutine.model

import com.blocksdecoded.coinwave.data.NetworkException
import com.blocksdecoded.core.network.NetworkError
import com.blocksdecoded.core.network.RHWithErrorHandler
import com.blocksdecoded.utils.coroutine.model.Result.Success
import com.blocksdecoded.utils.coroutine.model.Result.Error
import retrofit2.Call
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by askar on 11/21/18
 * with Android Studio
 */

inline fun <T : Any> Result<T>.onResult(action: (Result<T>) -> Unit): Result<T> {
    action.invoke(this)

    return this
}

inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Success) action(data)

    return this
}

inline fun <T : Any> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Error && exception != null) action(exception)

    return this
}

inline fun <T : Any, R : Any> Result<T>.mapOnSuccess(map: (T) -> R) = when (this) {
    is Success -> Success(map(data))
    is Error -> this
}

suspend fun <T : Any> Call<T>.getResult(): Result<T> = suspendCoroutine {
        enqueue(object : RHWithErrorHandler<T> {
            override fun onSuccess(result: T) {
                it.resume(Result.Success(result))
            }

            override fun onFailure(error: NetworkError) {
                it.resume(Error(NetworkException(error.toString())))
            }
        })
    }