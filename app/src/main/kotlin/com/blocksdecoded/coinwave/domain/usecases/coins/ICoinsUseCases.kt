package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable

// Created by askar on 7/19/18.
interface ICoinsUseCases {
    fun getCoins(skipCache: Boolean): Observable<CoinsResult>

    fun getCoin(id: Int): CoinEntity?

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean

    fun addObserver(observer: ICoinsObserver)

    fun removeObserver(observer: ICoinsObserver)
}