package com.makeuseof.utils.retrofit

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.utils.coroutine.model.Result.*
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.NetworkException
import com.squareup.moshi.Moshi
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
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

    fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                .build()
    }

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