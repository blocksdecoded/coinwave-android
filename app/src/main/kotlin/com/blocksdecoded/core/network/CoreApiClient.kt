package com.blocksdecoded.core.network

import com.google.gson.GsonBuilder
import com.blocksdecoded.coinwave.CoinApp
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by askar on 11/21/18
 * with Android Studio
 */
abstract class CoreApiClient {

    private fun getRetrofit(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ChuckInterceptor(CoinApp.INSTANCE))
                .writeTimeout(120, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
    }

    fun <T> getRetrofitClient(
        baseUrl: String,
        client: Class<T>
    ): T = getRetrofit(baseUrl).create(client)
}