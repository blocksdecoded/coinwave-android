package com.blocksdecoded.coinwave.data.coins.chart

import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.chart.ChartData
import io.reactivex.Single

interface IChartsStorage {
    fun getChart(chartName: String, period: ChartPeriodEnum = ChartPeriodEnum.DAY): Single<ChartData>
}