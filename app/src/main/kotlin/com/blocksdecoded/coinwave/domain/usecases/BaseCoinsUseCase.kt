package com.blocksdecoded.coinwave.domain.usecases

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable

interface BaseCoinsUseCase {
    fun getCoins(skipCache: Boolean, force: Boolean = false): Observable<CoinsResult>

    fun getCoin(id: Int): CoinEntity?
}