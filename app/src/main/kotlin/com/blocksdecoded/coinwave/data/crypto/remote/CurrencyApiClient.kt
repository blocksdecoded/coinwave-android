package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.model.ChartResponse
import com.blocksdecoded.coinwave.data.model.CurrencyListResponse
import com.blocksdecoded.core.network.CoreApiClient
import com.blocksdecoded.utils.coroutine.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
internal object CurrencyApiClient : CoreApiClient(), CurrencyClient {
    private val mClient: CurrencyNetworkClient

    init {
        mClient = getRetrofitClient(
                CurrencyNetworkClient.BASE_URL,
                CurrencyNetworkClient::class.java
        )
    }

    //region Public

    override suspend fun getCurrencies(pageSize: Int): Result<CurrencyListResponse> =
            mClient.getCurrencies(pageSize).getResult()

    override suspend fun getCurrencies(pageSize: Int, ids: String): Result<CurrencyListResponse> =
            mClient.getCurrencies(pageSize, ids).getResult()

    override suspend fun getHistory(chartName: String, period: ChartPeriodEnum): Result<ChartResponse> =
            mClient.getChartForTime(chartName, period.displayName).getResult()

    //endregion

    private interface CurrencyNetworkClient {

        @GET(CURRENCIES_PATH)
        fun getCurrencies(@Query("limit") limit: Int): Call<CurrencyListResponse>

        @GET(CURRENCIES_PATH)
        fun getCurrencies(
            @Query("limit") limit: Int,
            @Query("ids") ids: String
        ): Call<CurrencyListResponse>

        @GET("$HISTORY_PATH/{coin}/history/{period}/index.json")
        fun getChartForTime(
            @Path("coin") currency: String,
            @Path("period") period: String
        ): Call<ChartResponse>

        companion object {
            const val BASE_URL = BuildConfig.API_CURRENCY

            private const val PREFIX_PATH = "/ipns/QmURdeZbZxv3qwP6NRtH1WGRbcY4eiZsG4rEyDtFa2vgwW"
            private const val CURRENCIES_PATH = "$PREFIX_PATH/index.json"
            private const val HISTORY_PATH = "$PREFIX_PATH/coin"
        }
    }
}