package com.makeuseof.cryptocurrency.domain.usecases.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.ChartData

// Created by askar on 7/25/18.
interface ChartsUseCases {
    enum class ChartPeriod{
        TODAY,
        WEEK,
        MONTH_1,
        MONTH_3,
        MONTH_6,
        YEAR,
        ALL
    }
    suspend fun getChartData(currencyId: Int, period: ChartPeriod = ChartPeriod.ALL): Result<ChartData>
}