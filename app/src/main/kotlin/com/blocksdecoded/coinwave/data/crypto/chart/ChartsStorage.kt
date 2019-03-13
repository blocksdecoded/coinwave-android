package com.blocksdecoded.coinwave.data.crypto.chart

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClient
import com.blocksdecoded.coinwave.data.model.ChartData
import io.reactivex.Single

class ChartsStorage(
    private val coinApiClient: ICoinClient
) : IChartsStorage {
    override fun getChart(chartName: String, period: ChartPeriodEnum): Single<ChartData> =
            coinApiClient.getHistory(chartName, period)
                .map { ChartData(it.data.history) }
}