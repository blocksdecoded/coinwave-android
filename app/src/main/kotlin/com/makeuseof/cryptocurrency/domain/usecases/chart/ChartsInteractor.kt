package com.makeuseof.cryptocurrency.domain.usecases.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.EmptyCache
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.chart.ChartsSourceContract
import com.makeuseof.cryptocurrency.data.crypto.CurrencySourceContract
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsUseCases.ChartPeriod
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import com.makeuseof.utils.Lg
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.experimental.withContext
import java.util.*
import kotlin.collections.HashMap

// Created by askar on 7/25/18.
class ChartsInteractor(
        private val appExecutors: AppExecutors,
        private val mCryptoService: CurrencySourceContract,
        private val mChartsService: ChartsSourceContract
): ChartsUseCases {
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

    private fun applyPeriod(data: ChartData, period: ChartPeriod): ChartData{
        val date = Date(data.usdChart.last()[0].toLong())

        if (period == ALL) return data

        val time = System.currentTimeMillis() - getChartPeriodTime(period)

        Lg.d("$date - ${Date(time)}")

        val result = arrayListOf<List<Double>>()

        result.addAll( data.usdChart.filter { it[0] > time } )

        return ChartData(result)
    }

    override suspend fun getChartData(currencyId: Int, period: ChartPeriod): Result<ChartData> = withContext(appExecutors.ioContext) {
        if (cachedChart[period.toString()] == null){
            val currency = mCryptoService.getCurrency(currencyId)
            if(currency != null){
                val result = mChartsService.getChart(currency.name.toLowerCase())
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