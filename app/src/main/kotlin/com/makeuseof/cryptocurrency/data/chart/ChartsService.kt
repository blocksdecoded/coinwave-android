package com.makeuseof.cryptocurrency.data.chart

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.chart.model.ChartPeriod
import com.makeuseof.cryptocurrency.data.chart.network.ChartsConfig
import com.makeuseof.cryptocurrency.data.chart.network.ChartsNetworkClient
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.utils.Lg
import com.makeuseof.utils.coroutine.model.mapOnSuccess
import com.makeuseof.utils.retrofit.BaseRetrofitDataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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