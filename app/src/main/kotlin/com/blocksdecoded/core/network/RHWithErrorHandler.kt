package com.blocksdecoded.core.network

import com.blocksdecoded.utils.Lg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface RHWithErrorHandler<T> : Callback<T> {
    fun onSuccess(result: T)

    fun onFailure(error: NetworkError)

    private fun setError(error: NetworkError) {
        NetworkErrorHandler.getInstance()?.handleError(error)
        onFailure(error)
    }

    private fun handleUnauthorized() {
        NetworkErrorHandler.getInstance()?.handleError(NetworkError.UNAUTHORIZED)
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?): Unit = if (response != null) {
//        Lg.d(call?.request()?.url().toString() + ", code is " + response.code())
        when (response.code()) {
            in 200..300 -> { val result = response.body()
                if (result != null) {
                    onSuccess(result)
                } else {
                    setError(NetworkError.EMPTY_BODY)
                }
            }

            404 -> { setError(NetworkError.CONTENT_NOT_FOUND) }
            500 -> {
                Lg.d(response.message())
                setError(NetworkError.SERVER_ERROR)
            }
            400 -> { setError(NetworkError.ACTION_ERROR) }
            502, 503 -> { setError(NetworkError.SERVER_MAINTENANCE) }
            401 -> { handleUnauthorized() }
            422 -> { setError(NetworkError.INCORRECT_INPUT) }

            else -> { setError(NetworkError.UNHANDLED_ERROR) }
        }
    } else {
        setError(NetworkError.NULL_RESPONSE)
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        setError(NetworkError.CONNECTION_ERROR)
    }
}