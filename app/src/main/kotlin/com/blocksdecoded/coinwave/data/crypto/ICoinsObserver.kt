package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult

// Created by askar on 7/20/18.
interface ICoinsObserver {
    fun onAdded(coinEntity: CoinEntity)

    fun onUpdated(coins: CoinsResult)

    fun onRemoved(coinEntity: CoinEntity)
}