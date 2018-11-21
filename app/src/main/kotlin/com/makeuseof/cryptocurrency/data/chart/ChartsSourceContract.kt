package com.makeuseof.cryptocurrency.data.chart

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.cryptocurrency.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsSourceContract {
    suspend fun getChart(chartName: String, fromTime: Long = 0L, toTime: Long = 0L): Result<ChartData>
}