package com.blocksdecoded.coinwave.data.crypto.chart

import com.blocksdecoded.coinwave.data.crypto.chart.model.ChartPeriodEnum
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsSourceContract {
    suspend fun getChart(
            chartName: String,
            period: ChartPeriodEnum = ChartPeriodEnum.DAY
    ): Result<ChartData>
}