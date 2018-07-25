package com.makeuseof.cryptocurrency.data.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsSourceContract {
    suspend fun getChart(chartName: String): Result<ChartData>
}