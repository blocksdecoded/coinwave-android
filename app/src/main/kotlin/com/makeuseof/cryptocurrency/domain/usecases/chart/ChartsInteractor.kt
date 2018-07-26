package com.makeuseof.cryptocurrency.domain.usecases.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.EmptyCache
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.chart.ChartsSourceContract
import com.makeuseof.cryptocurrency.data.crypto.CurrencySourceContract
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsUseCases.ChartPeriod
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.experimental.withContext

// Created by askar on 7/25/18.
class ChartsInteractor(
        private val appExecutors: AppExecutors,
        private val mCryptoService: CurrencySourceContract,
        private val mChartsService: ChartsSourceContract
): ChartsUseCases {
    private var cachedChart: ChartData? = null

    private fun applyPeriod(data: ChartData, period: ChartPeriod): ChartData{
        val result = arrayListOf<List<Double>>()
        result.addAll(data.usdChart)
        return ChartData(result)
    }

    override suspend fun getChartData(currencyId: Int, period: ChartPeriod): Result<ChartData> = withContext(appExecutors.ioContext) {
        if (cachedChart == null){
            val currency = mCryptoService.getCurrency(currencyId)
            if(currency != null){
                val result = mChartsService.getChart(currency.name.toLowerCase())
                when(result){
                    is Result.Success -> {
                        cachedChart = result.data
                        Result.Success(applyPeriod(result.data, period))
                    }
                    else -> {
                        Result.Error(NetworkException("Unknown error"))
                    }
                }
            } else {
                Result.Error(EmptyCache("Currency not found"))
            }
        } else {
            Result.Success(applyPeriod(cachedChart!!, period))
        }
    }
}