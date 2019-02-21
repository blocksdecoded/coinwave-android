package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.EmptyCache
import com.blocksdecoded.coinwave.data.NetworkException
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsSourceContract
import com.blocksdecoded.coinwave.data.crypto.CurrencySourceContract
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import com.blocksdecoded.utils.coroutine.AppExecutors
import kotlinx.coroutines.withContext
import kotlin.collections.HashMap

import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum as RequestChartPeriod

// Created by askar on 7/25/18.
class ChartsInteractor(
        private val mCryptoService: CurrencySourceContract,
        private val mChartsService: ChartsSourceContract
): ChartsUseCases {
    private var mCachedId: Int = -1
    private var cachedChart: HashMap<String, ChartData> = HashMap()

    private fun getChartPeriodTime(period: ChartPeriod): Long{
        val day = 1000 * 60 * 60 * 24L
        return when(period){
            TODAY -> day
            WEEK -> day * 7
            MONTH_1 -> day * 30
            MONTH_3 -> day * 90
            MONTH_6 -> day * 180
            YEAR -> day * 365
            ALL -> 0
        }
    }

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

    override suspend fun getChartData(currencyId: Int, period: ChartPeriod): Result<ChartData> = withContext(AppExecutors.ioContext) {
        if (mCachedId != currencyId) {
            mCachedId = currencyId
            cachedChart.clear()
        }

        if (cachedChart[period.toString()] == null){
            val currency = mCryptoService.getCurrency(currencyId)
            if(currency != null){
                val result = mChartsService.getChart(currency.symbol, getRequestPeriod(period))
                when(result){
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