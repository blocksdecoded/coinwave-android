package com.blocksdecoded.coinwave.domain.usecases.list

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.crypto.CurrencyUpdateObserver
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

// Created by askar on 7/19/18.
interface CurrencyListUseCases {
    suspend fun getCryptoList(skipCache: Boolean): Result<List<CurrencyEntity>>

    fun getCurrency(id: Int): CurrencyEntity?

    fun saveCurrency(id: Int): Boolean

    fun removeCurrency(id: Int): Boolean

    fun addObserver(observer: CurrencyUpdateObserver)

    fun removeObserver(observer: CurrencyUpdateObserver)
}