package com.blocksdecoded.core.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.blocksdecoded.core.network.NetworkError.*

class NetworkErrorHandler(private val mContext: Context?) {

	companion object {
		@SuppressLint("StaticFieldLeak")
		private var INSTANCE: NetworkErrorHandler? = null

		fun getInstance(context: Context? = null): NetworkErrorHandler?{
			if (context != null && INSTANCE == null)
				INSTANCE = NetworkErrorHandler(context.applicationContext)

			if (context == null && INSTANCE == null)
				Log.e("ololo", "Try get network error handler without context")

			return INSTANCE
		}

		fun destroyInstance(){
			INSTANCE = null
		}
	}

	fun handleError(error: NetworkError) = when(error){
		UNAUTHORIZED -> { onUnauthorized() }
		CONTENT_NOT_FOUND -> {}
		CONNECTION_ERROR -> {}
		ACTION_ERROR -> {}
		NULL_RESPONSE -> {}
		EMPTY_BODY -> {}
		SERVER_MAINTENANCE -> {}
		SERVER_ERROR -> {}
		UNHANDLED_ERROR -> {}
		INCORRECT_INPUT -> {}
		FORBIDDEN -> {}
	}

	private fun onUnauthorized(){
		(mContext as? Activity)?.let{
			Toast.makeText(it, "You are unauthorized,\nplease authorize", Toast.LENGTH_LONG).show()
		}
	}
}