package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.data.model.CurrencyListResponse

// Created by askar on 7/19/18.
interface CurrencySourceContract {
    suspend fun getAllCurrencies(skipCache: Boolean): Result<CurrencyListResponse>

    fun getCurrency(id: Int): CurrencyEntity?

    fun saveCurrency(id: Int): Boolean

    fun removeCurrency(id: Int): Boolean

    fun addCurrencyObserver(observer: CurrencyUpdateObserver)

    fun removeCurrencyObserver(observer: CurrencyUpdateObserver)
}