package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.data.crypto.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.CoinsResponse
import io.reactivex.Observable
import io.reactivex.Single

interface ICoinClient {
    fun getCoins(pageSize: Int): Observable<CoinsResponse>

    fun getHistory(chartName: String, period: ChartPeriodEnum): Single<HistoryResponse>
}