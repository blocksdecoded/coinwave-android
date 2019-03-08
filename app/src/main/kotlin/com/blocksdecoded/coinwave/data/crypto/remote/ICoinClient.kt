package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.data.crypto.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.CoinsResponse
import com.blocksdecoded.utils.coroutine.model.Result

interface ICoinClient {
    suspend fun getCoins(pageSize: Int): Result<CoinsResponse>

    suspend fun getCoins(pageSize: Int, ids: String): Result<CoinsResponse>

    suspend fun getHistory(chartName: String, period: ChartPeriodEnum): Result<HistoryResponse>
}