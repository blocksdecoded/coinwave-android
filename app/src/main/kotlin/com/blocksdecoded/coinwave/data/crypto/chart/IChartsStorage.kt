package com.blocksdecoded.coinwave.data.crypto.chart

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.ChartData
import io.reactivex.Single

// Created by askar on 7/25/18.
interface IChartsStorage {
    fun getChart(
        chartName: String,
        period: ChartPeriodEnum = ChartPeriodEnum.DAY
    ): Single<ChartData>
}