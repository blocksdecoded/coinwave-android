package com.blocksdecoded.utils.retrofit

import com.google.gson.GsonBuilder
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.Result.*
import com.blocksdecoded.core.network.NetworkError
import com.blocksdecoded.core.network.RHWithErrorHandler
import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.data.NetworkException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by askar on 11/21/18
 * with Android Studio
 */
abstract class BaseRetrofitDataSource {

    private fun getRetrofit(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .apply {
                    if (BuildConfig.DEBUG) {
                        client(httpClient.build())
                    }
                }
                .build()
    }

    fun <T> getRetrofitClient(
        baseUrl: String,
        client: Class<T>
    ): T = getRetrofit(baseUrl).create(client)

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
}