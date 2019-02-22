package com.blocksdecoded.coinwave.data.crypto.chart

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess

// Created by askar on 7/25/18.
object ChartsService : ChartsSourceContract {
    override suspend fun getChart(chartName: String, period: ChartPeriodEnum): Result<ChartData> =
            CoinApiClient.getHistory(chartName, period)
                    .mapOnSuccess { ChartData(it.data.history) }
}