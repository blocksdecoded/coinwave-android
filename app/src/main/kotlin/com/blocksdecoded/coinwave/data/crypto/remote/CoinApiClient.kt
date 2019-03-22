package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.CoinsResponse
import com.blocksdecoded.core.network.CoreApiClient
import com.blocksdecoded.utils.logE
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception
import java.util.concurrent.TimeoutException

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
class CoinApiClient(
    private val config: ICoinClientConfig
) : CoreApiClient(), ICoinClient {
    private val mClient: CurrencyNetworkClient

    init {
        mClient = getRetrofitClient(
                config.coinUrl,
                CurrencyNetworkClient::class.java
        )
    }

    //region Public

    override fun getCoins(pageSize: Int): Observable<CoinsResponse> = mClient.getCoins(
        config.ipnsKey,
        pageSize
    ).retry { t1, t2 ->
        logE(Exception(t2))
        when (t2) {
            is TimeoutException -> true
            else -> false
        }
    }.doOnError { logE(Exception(it)) }

    override fun getHistory(chartName: String, period: ChartPeriodEnum) = mClient.getChartForTime(
        config.ipnsKey,
        chartName,
        period.displayName
    ).retry { t1, t2 ->
        logE(Exception(t2))
        when (t2) {
            is TimeoutException -> true
            else -> false
        }
    }.doOnError { logE(Exception(it)) }

    //endregion

    private interface CurrencyNetworkClient {
        @GET(COINS_PATH)
        fun getCoins(
            @Path(KEY) ipnsPath: String,
            @Query(LIMIT) limit: Int
        ): Observable<CoinsResponse>

        @GET(HISTORY_PATH)
        fun getChartForTime(
            @Path(KEY) ipnsPath: String,
            @Path(COIN) coin: String,
            @Path(PERIOD) period: String
        ): Single<HistoryResponse>

        companion object {
            private const val KEY = "key"
            private const val COIN = "coin"
            private const val PERIOD = "period"
            private const val LIMIT = "limit"

            private const val PREFIX_PATH = "/ipns/{$KEY}"
            private const val COINS_PATH = "$PREFIX_PATH/index.json"
            private const val HISTORY_PATH = "$PREFIX_PATH/coin/{$COIN}/history/{$PERIOD}/index.json"
        }
    }
}