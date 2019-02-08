package com.blocksdecoded.core.network

import android.util.Log
import com.blocksdecoded.utils.Lg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SimpleRH<T>: Callback<T> {
	fun onSuccess(result: T)

	fun onFailure(error: NetworkError)

	override fun onResponse(call: Call<T>?, response: Response<T>?) = if (response != null){
		Lg.d(call?.request()?.url().toString() + " " + response.code())
		when (response.code()){
			in 200..300 -> { val result = response.body()
				if (result != null){
					onSuccess(result)
				}else{
					onFailure(NetworkError.EMPTY_BODY)
				}
			}

			404 -> { onFailure(NetworkError.CONTENT_NOT_FOUND) }
			500 -> { onFailure(NetworkError.SERVER_ERROR) }
			400 -> { onFailure(NetworkError.ACTION_ERROR) }
			401 -> { onFailure(NetworkError.UNAUTHORIZED) }
			422 -> { onFailure(NetworkError.INCORRECT_INPUT)}
			403 -> { onFailure(NetworkError.FORBIDDEN)}

			else -> { onFailure(NetworkError.UNHANDLED_ERROR) }
		}
	}else{
		onFailure(NetworkError.NULL_RESPONSE)
	}

	override fun onFailure(call: Call<T>?, t: Throwable?) {
		Log.e("ololo", t?.message)
		onFailure(NetworkError.CONNECTION_ERROR)
	}
}