package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.CoinsResponse
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
internal object CoinApiClient : CoreApiClient(), ICoinClient {
    private val mClient: CurrencyNetworkClient

    init {
        mClient = getRetrofitClient(
                CurrencyNetworkClient.BASE_URL,
                CurrencyNetworkClient::class.java
        )
    }

    //region Public

    override suspend fun getCoins(pageSize: Int): Result<CoinsResponse> =
            mClient.getCoins(pageSize).getResult()

    override suspend fun getCoins(pageSize: Int, ids: String): Result<CoinsResponse> =
            mClient.getCoins(pageSize, ids).getResult()

    override suspend fun getHistory(chartName: String, period: ChartPeriodEnum): Result<HistoryResponse> =
            mClient.getChartForTime(chartName, period.displayName).getResult()

    //endregion

    private interface CurrencyNetworkClient {

        @GET(COINS_PATH)
        fun getCoins(@Query("limit") limit: Int): Call<CoinsResponse>

        @GET(COINS_PATH)
        fun getCoins(
            @Query("limit") limit: Int,
            @Query("ids") ids: String
        ): Call<CoinsResponse>

        @GET("$HISTORY_PATH/{coin}/history/{period}/index.json")
        fun getChartForTime(
            @Path("coin") coin: String,
            @Path("period") period: String
        ): Call<HistoryResponse>

        companion object {
            const val BASE_URL = BuildConfig.API_COINS

            private const val PREFIX_PATH = "/ipns/QmT3qT84rdZ9tkUssYLNkKbPcFTkutFy2S8uizvKWQbTMg"
            private const val COINS_PATH = "$PREFIX_PATH/index.json"
            private const val HISTORY_PATH = "$PREFIX_PATH/coin"
        }
    }
}