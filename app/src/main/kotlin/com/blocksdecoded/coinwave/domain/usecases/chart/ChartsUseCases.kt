package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.model.ChartData
import io.reactivex.Single

// Created by askar on 7/25/18.
interface ChartsUseCases {
    enum class ChartPeriod {
        TODAY,
        WEEK,
        MONTH_1,
        MONTH_3,
        MONTH_6,
        YEAR,
        ALL
    }

    fun getChartData(coinId: Int, period: ChartPeriod = ChartPeriod.TODAY): Single<ChartData>
}