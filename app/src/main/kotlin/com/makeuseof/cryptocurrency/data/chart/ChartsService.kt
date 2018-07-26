package com.makeuseof.cryptocurrency.data.chart

import com.makeuseof.core.model.Result
import com.makeuseof.core.network.NetworkClientFactory
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.chart.network.ChartsConfig
import com.makeuseof.cryptocurrency.data.chart.network.ChartsNetworkClient
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.utils.Lg
import kotlin.coroutines.experimental.suspendCoroutine

// Created by askar on 7/25/18.
class ChartsService: ChartsSourceContract{
    private val mClient: ChartsNetworkClient = NetworkClientFactory.getRetrofitClient(
            ChartsNetworkClient::class.java,
            ChartsConfig.BASE_URL
    )

    companion object {
        private var INSTANCE: ChartsService? = null

        fun getInstance(): ChartsSourceContract{
            if(INSTANCE == null)
                INSTANCE = ChartsService()
            return INSTANCE!!
        }
    }

    override suspend fun getChart(chartName: String, fromTime: Long, toTime: Long): Result<ChartData> = suspendCoroutine {
        val call = if(fromTime == 0L){
            mClient.getChartForTime(chartName, fromTime, toTime)
        } else {
            mClient.getAllChartData(chartName)
        }

        call.enqueue(object : RHWithErrorHandler<ChartData> {
            override fun onSuccess(result: ChartData) {
                it.resume(Result.Success(result))
            }

            override fun onFailure(error: NetworkError) {
                Lg.d(error.toString())
                it.resume(Result.Error(NetworkException(error.toString())))
            }
        })
    }
}