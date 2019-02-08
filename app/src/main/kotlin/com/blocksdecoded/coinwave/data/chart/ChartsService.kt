package com.blocksdecoded.coinwave.data.chart

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.chart.model.ChartPeriod
import com.blocksdecoded.coinwave.data.chart.network.ChartsConfig
import com.blocksdecoded.coinwave.data.chart.network.ChartsNetworkClient
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.utils.retrofit.BaseRetrofitDataSource

// Created by askar on 7/25/18.
class ChartsService: BaseRetrofitDataSource(), ChartsSourceContract {
    private val mClient = getRetrofitClient(
            ChartsConfig.BASE_URL,
            ChartsNetworkClient::class.java
    )

    companion object {
        private var INSTANCE: ChartsService? = null

        fun getInstance(): ChartsSourceContract{
            if(INSTANCE == null)
                INSTANCE = ChartsService()
            return INSTANCE!!
        }
    }

    override suspend fun getChart(chartName: String, period: ChartPeriod): Result<ChartData> =
            mClient.getChartForTime(chartName, period.representation)
                    .getResult()
                    .mapOnSuccess { ChartData(it.data.history) }
}