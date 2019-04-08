package com.blocksdecoded.coinwave.data.coins.remote

import com.blocksdecoded.coinwave.data.coins.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.CoinsResponse
import io.reactivex.Single

interface ICoinClient {
    fun getCoins(): Single<CoinsResponse>

    fun getHistory(coin: String, period: ChartPeriodEnum): Single<HistoryResponse>
}