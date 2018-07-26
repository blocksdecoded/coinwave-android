package com.makeuseof.cryptocurrency.domain.usecases.chart

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.EmptyCache
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
    override suspend fun getChartData(currencyId: Int, period: ChartPeriod): Result<ChartData> = withContext(appExecutors.ioContext) {
        val currency = mCryptoService.getCurrency(currencyId)
        if(currency != null){
            mChartsService.getChart(currency.name.toLowerCase())
        } else {
            Result.Error(EmptyCache("Currency not found"))
        }
    }
}