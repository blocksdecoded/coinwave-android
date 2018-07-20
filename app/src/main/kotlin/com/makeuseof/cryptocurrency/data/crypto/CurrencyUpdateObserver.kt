package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

// Created by askar on 7/20/18.
interface CurrencyUpdateObserver {
    fun onAdded(currencyEntity: CurrencyEntity)

    fun onUpdated(currencies: List<CurrencyEntity>)

    fun onRemoved(currencyEntity: CurrencyEntity)
}