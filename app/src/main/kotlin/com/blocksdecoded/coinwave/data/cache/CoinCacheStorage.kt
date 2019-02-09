package com.blocksdecoded.coinwave.data.cache

import com.blocksdecoded.coinwave.data.crypto.CurrencyUpdateObserver
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

/**
 * Created by askar on 2/9/19
 * with Android Studio
 */
class CoinCacheStorage: CoinCache {

    private val mCache = HashMap<Int, CurrencyEntity>()
    private val mObservers = hashSetOf<CurrencyUpdateObserver>()

    override fun setCache(coins: List<CurrencyEntity>) {
        coins.forEach { currency ->
            mCache[currency.id] = currency
            mObservers.forEach { it.onAdded(currency) }
        }
        mObservers.forEach { it.onUpdated(coins) }
    }

    override fun addCacheObserver() {

    }

    override fun removeCacheObserver() {

    }

}