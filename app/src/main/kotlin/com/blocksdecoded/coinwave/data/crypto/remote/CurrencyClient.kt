package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.data.crypto.remote.model.ChartResponse
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.CurrencyListResponse
import com.blocksdecoded.utils.coroutine.model.Result

interface CurrencyClient {
    suspend fun getCurrencies(pageSize: Int): Result<CurrencyListResponse>

    suspend fun getCurrencies(pageSize: Int, ids: String): Result<CurrencyListResponse>

    suspend fun getHistory(chartName: String, period: ChartPeriodEnum): Result<ChartResponse>
}