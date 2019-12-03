package com.blocksdecoded.coinwave.data.coins.remote

import com.blocksdecoded.coinwave.data.coins.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.coin.CoinsResponse
import com.blocksdecoded.core.network.CoreApiClient
import com.blocksdecoded.utils.logE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeoutException

/**
 * Created by askar on 2/12/19
 * with Android Studio
 */
class CoinApiClient(
    private val config: ICoinClientConfig
) : CoreApiClient(), ICoinClient {
    private val mClient: CurrencyNetworkClient = getRetrofitClient(
        config.ipfsUrl,
        CurrencyNetworkClient::class.java
    )

    //region Public

    private fun <T> Single<T>.timeoutRetry(): Single<T> = this.retry { _, t2 ->
        logE(Exception(t2))
        when (t2) {
            is TimeoutException -> true
            else -> false
        }
    }

    override fun getCoins(): Single<CoinsResponse> =
        mClient.getCoins("${config.ipnsPath}index.json")
            .timeoutRetry()

    override fun getHistory(coin: String, period: ChartPeriodEnum) =
        mClient.getChart("${config.ipnsPath}coin/$coin/history/${period.displayName}/index.json")
            .timeoutRetry()

    //endregion

    private interface CurrencyNetworkClient {
        @GET
        fun getCoins(@Url url: String): Single<CoinsResponse>

        @GET
        fun getChart(@Url url: String): Single<HistoryResponse>

        companion object {
            private const val IPNS = "ipns"
            private const val COIN = "coin"
            private const val PERIOD = "period"

            private const val COINS_PATH = "/index.json"
            private const val HISTORY_PATH = "/coin/{$COIN}/history/{$PERIOD}/index.json"
        }
    }
}