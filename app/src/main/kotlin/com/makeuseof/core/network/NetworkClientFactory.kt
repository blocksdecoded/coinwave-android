package com.makeuseof.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Tameki on 1/15/18.
 */
object NetworkClientFactory {

	fun <T> getRetrofitClient(client: Class<T>, baseUrl: String):T{
		val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())

		val retrofit: Retrofit = retrofitBuilder.build()

		return retrofit.create(client)
	}
}