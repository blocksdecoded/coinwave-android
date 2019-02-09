package com.blocksdecoded.coinwave.data.cache

import com.blocksdecoded.coinwave.data.model.CurrencyEntity

/**
 * Created by askar on 2/9/19
 * with Android Studio
 */
interface CoinCache {
    fun setCache(coins: List<CurrencyEntity>)

    fun addCacheObserver()

    fun removeCacheObserver()
}