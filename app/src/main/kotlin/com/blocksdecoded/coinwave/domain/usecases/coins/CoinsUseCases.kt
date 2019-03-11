package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import io.reactivex.Flowable

// Created by askar on 7/19/18.
interface CoinsUseCases {
    fun getCoins(skipCache: Boolean): Flowable<List<CoinEntity>>

    fun getCoin(id: Int): CoinEntity?

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean

    fun addObserver(observer: ICoinsObserver)

    fun removeObserver(observer: ICoinsObserver)
}