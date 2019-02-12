package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.EmptyCache
import com.blocksdecoded.coinwave.data.cache.CoinCache
import com.blocksdecoded.coinwave.data.crypto.remote.CurrencyApiClient
import com.blocksdecoded.coinwave.data.model.CurrencyDataResponse
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess

// Created by askar on 7/19/18.
class CurrencyRepository(
        private val mWatchlistSource: WatchlistSourceContract
): CurrencySourceContract {
    private var mCached: CurrencyDataResponse? = null
    private val mObservers = hashSetOf<CurrencyUpdateObserver>()

    companion object {
        private const val NETWORK_PAGE_SIZE = 100
        private const val CURRENT_PAGE = 0
        private var INSTANCE: CurrencyRepository? = null

        fun getInstance(
                watchlist: WatchlistSourceContract
        ): CurrencySourceContract {
            if (INSTANCE == null){
                INSTANCE = CurrencyRepository(watchlist)
            }
            return INSTANCE!!
        }
    }

    //region Private

    private fun setCache(data: CurrencyDataResponse) {
        markSaved(data.coins)
        mCached = data
        mObservers.forEach { it.onUpdated(data.coins) }
    }

    private fun markSaved(currencies: List<CurrencyEntity>) {
        val saved = mWatchlistSource.getAll()
        currencies.forEach {
            it.isSaved = saved.contains(it.id)
        }
    }

    private fun notifyAdded(currencyEntity: CurrencyEntity) {
        mObservers.forEach { it.onAdded(currencyEntity) }
    }

    private fun notifyRemoved(currencyEntity: CurrencyEntity) {
        mObservers.forEach { it.onRemoved(currencyEntity) }
    }

    private fun findCurrency(id: Int, onFind: (currency: CurrencyEntity) -> Unit): Boolean {
        val currency = mCached?.coins?.first { it.id == id }
        return if(currency != null){
            onFind.invoke(currency)
            true
        } else {
            false
        }
    }

    //endregion

    //region Contract

    override fun getCurrency(id: Int): CurrencyEntity? {
        return mCached?.coins?.first { it.id == id }
    }

    override fun addCurrencyObserver(observer: CurrencyUpdateObserver) {
        mObservers.add(observer)
    }

    override fun removeCurrencyObserver(observer: CurrencyUpdateObserver) {
        mObservers.remove(observer)
    }

    override fun saveCurrency(id: Int): Boolean = findCurrency(id) {
        mWatchlistSource.addId(id)
        it.isSaved = true
        notifyAdded(it)
    }

    override fun removeCurrency(id: Int): Boolean = findCurrency(id) {
        mWatchlistSource.deleteId(id)
        it.isSaved = false
        notifyRemoved(it)
    }

    override suspend fun getAllCurrencies(skipCache: Boolean): Result<CurrencyDataResponse> =
            if (skipCache){
                CurrencyApiClient.getCurrencies(NETWORK_PAGE_SIZE)
                        .onSuccess { setCache(it.data) }
                        .onError { mCached?.let { setCache(it) } }
                        .mapOnSuccess { it.data }
            } else {
                if (mCached != null){
                    Result.Success(mCached!!)
                } else {
                    Result.Error(EmptyCache("Cache is empty"))
                }
            }

    override suspend fun getWatchlist(skipCache: Boolean): Result<CurrencyDataResponse> =
            if (skipCache){
                var ids = ""
                mWatchlistSource.getAll().forEachIndexed { index, i ->
                    ids += when (index) {
                        0 -> "$i"
                        else -> ",$i"
                    }
                }
                CurrencyApiClient.getCurrencies(NETWORK_PAGE_SIZE, ids)
                        .onSuccess { setCache(it.data) }
                        .onError { mCached?.let { setCache(it) } }
                        .mapOnSuccess { it.data }
            } else {
                if (mCached != null){
                    Result.Success(mCached!!)
                } else {
                    Result.Error(EmptyCache("Cache is empty"))
                }
            }

    //endregion
}