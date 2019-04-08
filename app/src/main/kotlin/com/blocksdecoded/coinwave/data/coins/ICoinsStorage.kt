package com.blocksdecoded.coinwave.data.coins

import com.blocksdecoded.coinwave.data.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

// Created by askar on 7/19/18.
interface ICoinsStorage {
    val coinsUpdateSubject: PublishSubject<CoinsResult>

    fun getAllCoins(skipCache: Boolean, force: Boolean): Observable<CoinsDataResponse>

    fun getWatchlist(skipCache: Boolean): Observable<CoinsDataResponse>

    fun getChart(coin: String, period: ChartPeriodEnum = ChartPeriodEnum.DAY): Single<ChartData>

    fun setCoinsData(coinsData: CoinsDataResponse)

    fun getCoin(id: Int): CoinEntity?

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean
}