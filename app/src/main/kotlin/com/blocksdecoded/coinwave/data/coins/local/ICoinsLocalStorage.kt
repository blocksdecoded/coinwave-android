package com.blocksdecoded.coinwave.data.coins.local

import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import io.reactivex.Observable

interface ICoinsLocalStorage {
    fun setCoinsData(coinsData: CoinsDataResponse)

    fun getAllCoins(): Observable<CoinsDataResponse>
}