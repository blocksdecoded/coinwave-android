package com.blocksdecoded.coinwave.data.chart

import com.blocksdecoded.coinwave.data.chart.model.ChartPeriod
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsSourceContract {
    suspend fun getChart(chartName: String, period: ChartPeriod = ChartPeriod.DAY): Result<ChartData>
}