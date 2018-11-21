package com.makeuseof.core.model

import com.makeuseof.core.model.Result.Success
import com.makeuseof.core.model.Result.Error

/**
 * Created by askar on 11/21/18
 * with Android Studio
 */

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