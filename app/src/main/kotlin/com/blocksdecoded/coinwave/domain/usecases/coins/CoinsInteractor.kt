package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

// Created by askar on 7/19/18.
class CoinsInteractor(
    private val mCoinsRepository: ICoinsRepository
) : ICoinsUseCases {
    override val coinsObservable: Observable<CoinsResult>
        get() = mCoinsRepository.coinsUpdateSubject

    override fun getCoins(skipCache: Boolean, force: Boolean): Observable<CoinsResult> = mCoinsRepository.getAllCoins(skipCache, force)
        .map { CoinsResult(it.coins, it.updatedAt ?: Date()) }

    override fun getCoin(id: Int): CoinEntity? = mCoinsRepository.getCoin(id)

    override fun saveCoin(id: Int): Boolean = mCoinsRepository.saveCoin(id)

    override fun removeCoin(id: Int): Boolean = mCoinsRepository.removeCoin(id)
}