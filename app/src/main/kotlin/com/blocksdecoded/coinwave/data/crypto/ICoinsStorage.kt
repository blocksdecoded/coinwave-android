package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.model.CoinEntity

// Created by askar on 7/19/18.
interface ICoinsStorage {
    suspend fun getAllCoins(skipCache: Boolean): Result<CoinsDataResponse>

    suspend fun getWatchlist(skipCache: Boolean): Result<CoinsDataResponse>

    fun setCoinsData(coinsData: CoinsDataResponse)

    fun getCoin(id: Int): CoinEntity?

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean

    fun addCoinObserver(observer: ICoinsObserver)

    fun removeCoinObserver(observer: ICoinsObserver)
}