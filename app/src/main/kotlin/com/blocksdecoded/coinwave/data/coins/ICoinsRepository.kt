package com.blocksdecoded.coinwave.data.coins

import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.data.model.coin.CoinsDataResponse
import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

// Created by askar on 7/19/18.
interface ICoinsRepository {
    val coinsUpdateSubject: PublishSubject<CoinsResult>

    fun getAllCoins(skipCache: Boolean, force: Boolean): Observable<CoinsDataResponse>

    fun getChart(coin: String, period: ChartPeriodEnum = ChartPeriodEnum.DAY): Single<ChartData>

    fun getCoin(id: Int): CoinEntity?

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean
}