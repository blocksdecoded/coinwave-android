package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.EmptyCache
import com.blocksdecoded.coinwave.data.NetworkException
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsSourceContract
import com.blocksdecoded.coinwave.data.crypto.CoinsDataSource
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import com.blocksdecoded.utils.coroutine.AppExecutors
import kotlinx.coroutines.withContext
import kotlin.collections.HashMap

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum as RequestChartPeriod

// Created by askar on 7/25/18.
class ChartsInteractor(
    private val mCryptoService: CoinsDataSource,
    private val mChartsService: ChartsSourceContract
) : ChartsUseCases {
    private var mCachedId: Int = -1
    private var cachedChart: HashMap<String, ChartData> = HashMap()

    private fun getRequestPeriod(period: ChartPeriod): RequestChartPeriod {
        return when (period) {
            TODAY -> RequestChartPeriod.DAY
            WEEK -> RequestChartPeriod.WEEK
            MONTH_1 -> RequestChartPeriod.MONTH
            MONTH_3 -> RequestChartPeriod.MONTH
            MONTH_6 -> RequestChartPeriod.YEAR
            YEAR -> RequestChartPeriod.YEAR
            ALL -> RequestChartPeriod.FIVE_YEARS
        }
    }

    override suspend fun getChartData(coinId: Int, period: ChartPeriod): Result<ChartData> = withContext(AppExecutors.io) {
        if (mCachedId != coinId) {
            mCachedId = coinId
            cachedChart.clear()
        }

        if (cachedChart[period.toString()] == null) {
            val coin = mCryptoService.getCoin(coinId)
            if (coin != null) {
                val result = mChartsService.getChart(coin.symbol, getRequestPeriod(period))
                when (result) {
                    is Result.Success -> {
                        cachedChart[period.toString()] = result.data
                        Result.Success(result.data)
                    }
                    else -> {
                        Result.Error(NetworkException("Unknown error"))
                    }
                }
            } else {
                Result.Error(EmptyCache("Currency not found"))
            }
        } else {
            Result.Success(cachedChart[period.toString()]!!)
        }
    }
}