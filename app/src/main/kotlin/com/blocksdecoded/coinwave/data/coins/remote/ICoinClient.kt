package com.blocksdecoded.coinwave.data.coins.remote

import com.blocksdecoded.coinwave.data.coins.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.coin.CoinsResponse
import io.reactivex.Single

interface ICoinClient {
    fun getCoins(): Single<CoinsResponse>

    fun getHistory(coin: String, period: ChartPeriodEnum): Single<HistoryResponse>
}