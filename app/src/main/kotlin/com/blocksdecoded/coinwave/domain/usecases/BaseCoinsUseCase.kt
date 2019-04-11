package com.blocksdecoded.coinwave.domain.usecases

import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
import io.reactivex.Observable

interface BaseCoinsUseCase {
    fun getCoins(skipCache: Boolean, force: Boolean = false): Observable<CoinsResult>

    fun getCoin(id: Int): CoinEntity?
}