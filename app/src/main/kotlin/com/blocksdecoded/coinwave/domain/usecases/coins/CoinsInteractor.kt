package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.coins.ICoinsStorage
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

// Created by askar on 7/19/18.
class CoinsInteractor(
    private val mCoinsStorage: ICoinsStorage
) : ICoinsUseCases {
    override val coinsUpdateSubject: PublishSubject<CoinsResult>
        get() = mCoinsStorage.coinsUpdateSubject

    override fun getCoins(skipCache: Boolean, force: Boolean): Observable<CoinsResult> = mCoinsStorage.getAllCoins(skipCache, force)
        .map { CoinsResult(it.coins, it.updatedAt ?: Date()) }

    override fun getCoin(id: Int): CoinEntity? = mCoinsStorage.getCoin(id)

    override fun saveCoin(id: Int): Boolean = mCoinsStorage.saveCoin(id)

    override fun removeCoin(id: Int): Boolean = mCoinsStorage.removeCoin(id)
}