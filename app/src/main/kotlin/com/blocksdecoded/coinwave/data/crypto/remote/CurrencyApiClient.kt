package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.model.ChartResponse
import com.blocksdecoded.coinwave.data.model.CurrencyListResponse
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.getResult
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
internal object CurrencyApiClient {
    private val mClient: CurrencyNetworkClient

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logger)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()

        val retrofit = Retrofit.Builder()
                .baseUrl(CryptoConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

        mClient = retrofit.create(CurrencyNetworkClient::class.java)
    }

    //region Public

    suspend fun getCurrencies(pageSize: Int): Result<CurrencyListResponse> =
            mClient.getCurrencies(pageSize).getResult()

    suspend fun getCurrencies(pageSize: Int, ids: String): Result<CurrencyListResponse> =
            mClient.getCurrencies(pageSize, ids).getResult()

    suspend fun getHistory(chartName: String, period: ChartPeriodEnum): Result<ChartResponse> =
            mClient.getChartForTime(chartName, period.displayName).getResult()

    //endregion

    private interface CurrencyNetworkClient {
        @GET(CryptoConfig.CURRENCIES_PATH)
        fun getCurrencies(@Query("limit") limit: Int): Call<CurrencyListResponse>

        @GET(CryptoConfig.CURRENCIES_PATH)
        fun getCurrencies(
                @Query("limit") limit: Int,
                @Query("ids") ids: String
        ): Call<CurrencyListResponse>

        @GET("${CryptoConfig.HISTORY_PATH}/{coin}/history/{period}/index.json")
        fun getChartForTime(
                @Path("coin") currency: String,
                @Path("period") period: String
        ): Call<ChartResponse>
    }
}