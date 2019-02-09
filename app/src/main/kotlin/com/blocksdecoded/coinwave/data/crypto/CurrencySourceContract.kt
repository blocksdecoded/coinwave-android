package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.model.CurrencyDataResponse
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

// Created by askar on 7/19/18.
interface CurrencySourceContract {
    suspend fun getAllCurrencies(skipCache: Boolean): Result<CurrencyDataResponse>

    suspend fun getWatchlist(skipCache: Boolean): Result<CurrencyDataResponse>

    fun getCurrency(id: Int): CurrencyEntity?

    fun saveCurrency(id: Int): Boolean

    fun removeCurrency(id: Int): Boolean

    fun addCurrencyObserver(observer: CurrencyUpdateObserver)

    fun removeCurrencyObserver(observer: CurrencyUpdateObserver)
}