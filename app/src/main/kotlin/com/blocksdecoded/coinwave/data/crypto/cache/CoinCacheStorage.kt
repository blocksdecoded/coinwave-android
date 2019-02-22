package com.blocksdecoded.coinwave.data.crypto.cache

import com.blocksdecoded.coinwave.data.crypto.CoinsUpdateObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity

/**
 * Created by askar on 2/9/19
 * with Android Studio
 */
object CoinCacheStorage : CoinCache {
    private val mCache = HashMap<Int, CoinEntity>()
    private val mObservers = hashSetOf<CoinsUpdateObserver>()

    override fun setCache(coins: List<CoinEntity>) {
        coins.forEach { coin ->
            mCache[coin.id] = coin
            mObservers.forEach { it.onAdded(coin) }
        }
        mObservers.forEach { it.onUpdated(coins) }
    }

    override fun addObserver(observer: CoinCache.CacheObserver) {
    }

    override fun removeObserver(observer: CoinCache.CacheObserver) {
    }
}