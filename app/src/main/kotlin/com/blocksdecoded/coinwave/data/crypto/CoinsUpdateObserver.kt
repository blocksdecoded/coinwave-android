package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.model.CoinEntity

// Created by askar on 7/20/18.
interface CoinsUpdateObserver {
    fun onAdded(coinEntity: CoinEntity)

    fun onUpdated(coins: List<CoinEntity>)

    fun onRemoved(coinEntity: CoinEntity)
}