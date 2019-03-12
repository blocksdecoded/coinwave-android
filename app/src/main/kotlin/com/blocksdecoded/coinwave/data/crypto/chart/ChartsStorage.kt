package com.blocksdecoded.coinwave.data.crypto.chart

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.model.ChartData
import io.reactivex.Single


object ChartsStorage : IChartsStorage {
    override fun getChart(chartName: String, period: ChartPeriodEnum): Single<ChartData> =
            CoinApiClient.getHistory(chartName, period)
                .map { ChartData(it.data.history) }
}