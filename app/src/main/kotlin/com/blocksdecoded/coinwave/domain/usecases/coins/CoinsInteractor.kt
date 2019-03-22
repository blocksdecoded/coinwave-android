package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable
import java.util.*

// Created by askar on 7/19/18.
class CoinsInteractor(
    private val mCoinsSource: ICoinsStorage
) : ICoinsUseCases {
    override fun getCoins(skipCache: Boolean): Observable<CoinsResult> = mCoinsSource.getAllCoins(skipCache)
        .map { CoinsResult(it.coins, it.updatedAt ?: Date()) }

    override fun getCoin(id: Int): CoinEntity? {
        return mCoinsSource.getCoin(id)
    }

    override fun saveCoin(id: Int): Boolean {
        return mCoinsSource.saveCoin(id)
    }

    override fun removeCoin(id: Int): Boolean {
        return mCoinsSource.removeCoin(id)
    }

    override fun addObserver(observer: ICoinsObserver) {
        mCoinsSource.addCoinObserver(observer)
    }

    override fun removeObserver(observer: ICoinsObserver) {
        mCoinsSource.removeCoinObserver(observer)
    }
}