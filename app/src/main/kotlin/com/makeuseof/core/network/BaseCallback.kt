package com.makeuseof.core.network

/**
 * Created by Tameki on 2/23/18.
 */
interface BaseCallback {
	fun onSuccess()
	
	fun onFail(message: String?)
}