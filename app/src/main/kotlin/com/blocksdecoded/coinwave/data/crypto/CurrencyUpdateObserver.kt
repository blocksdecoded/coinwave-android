package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.model.CurrencyEntity

// Created by askar on 7/20/18.
interface CurrencyUpdateObserver {
    fun onAdded(currencyEntity: CurrencyEntity)

    fun onUpdated(currencies: List<CurrencyEntity>)

    fun onRemoved(currencyEntity: CurrencyEntity)
}