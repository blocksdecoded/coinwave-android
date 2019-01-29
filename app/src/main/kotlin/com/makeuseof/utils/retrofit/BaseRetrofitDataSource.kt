package com.makeuseof.utils.retrofit

import com.google.gson.GsonBuilder
import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.utils.coroutine.model.Result.*
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.NetworkException
import com.squareup.moshi.Moshi
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

    private fun getMoshi(): Moshi {
        return Moshi
                .Builder()
                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .build()
    }

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
                .client(httpClient.build())
                .build()
    }

    fun <T> getRetrofitClient(
            baseUrl: String,
            client: Class<T>
    ): T = getRetrofit(baseUrl).create(client)

    suspend fun <T: Any> Call<T>.getResult(): Result<T> = suspendCoroutine {
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