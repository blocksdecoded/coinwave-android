package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.crypto.CurrencyUpdateObserver
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

// Created by askar on 7/19/18.
interface CurrencyListUseCases {
    suspend fun getCryptoList(skipCache: Boolean): Result<List<CurrencyEntity>>

    fun getCurrency(id: Int): CurrencyEntity?

    fun saveCurrency(id: Int): Boolean

    fun removeCurrency(id: Int): Boolean

    fun addObserver(observer: CurrencyUpdateObserver)

    fun removeObserver(observer: CurrencyUpdateObserver)
}