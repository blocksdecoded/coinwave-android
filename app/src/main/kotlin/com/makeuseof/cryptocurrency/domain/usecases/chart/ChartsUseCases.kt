package com.makeuseof.cryptocurrency.domain.usecases.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsUseCases {
    suspend fun getChartData(currencyId: Int): Result<ChartData>
}