package com.blocksdecoded.coinwave.data.crypto.cache

import com.blocksdecoded.coinwave.data.model.CoinEntity

/**
 * Created by askar on 2/9/19
 * with Android Studio
 */
interface CoinCache {
    fun setCache(coins: List<CoinEntity>)

    fun addObserver(observer: CacheObserver)

    fun removeObserver(observer: CacheObserver)

    interface CacheObserver
}