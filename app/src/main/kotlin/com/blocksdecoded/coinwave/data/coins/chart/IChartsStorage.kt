package com.blocksdecoded.coinwave.data.coins.chart

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.ChartData
import io.reactivex.Single

interface IChartsStorage {
    fun getChart(chartName: String, period: ChartPeriodEnum = ChartPeriodEnum.DAY): Single<ChartData>
}