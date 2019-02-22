package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.EmptyCache
import com.blocksdecoded.coinwave.data.crypto.remote.CurrencyClient
import com.blocksdecoded.coinwave.data.model.CurrencyDataResponse
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess

// Created by askar on 7/19/18.
class CurrencyRepository(
    private val mCurrencyClient: CurrencyClient,
    private val mWatchlistSource: WatchlistSourceContract
) : CurrencySourceContract {
    private var mCached: CurrencyDataResponse? = null
    private val mObservers = hashSetOf<CurrencyUpdateObserver>()

    private val watchlistIds: String
        get() {
            var ids = ""
            mWatchlistSource.getAll().forEachIndexed { index, i ->
                ids += when (index) {
                    0 -> "$i"
                    else -> ",$i"
                }
            }
            return ids
        }

    //region Private


    //region Calls

    private suspend fun currenciesRequest(): Result<CurrencyDataResponse>
            = mCurrencyClient.getCurrencies(NETWORK_PAGE_SIZE)
                .onSuccess { setCache(it.data) }
                .onError { mCached?.let { setCache(it) } }
                .mapOnSuccess { it.data }

    private suspend fun watchlistRequest(): Result<CurrencyDataResponse>
            = mCurrencyClient.getCurrencies(NETWORK_PAGE_SIZE, watchlistIds)
                    .mapOnSuccess { it.data }

    //endregion

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

    private fun notifyAdded(currencyEntity: CurrencyEntity) =
            mObservers.forEach { it.onAdded(currencyEntity) }

    private fun notifyRemoved(currencyEntity: CurrencyEntity) =
            mObservers.forEach { it.onRemoved(currencyEntity) }

    private fun findCurrency(id: Int, onFind: (currency: CurrencyEntity) -> Unit): Boolean =
            mCached?.coins?.first { it.id == id }?.let {
                onFind.invoke(it)
                true
            } ?: false

    //endregion

    //region Contract

    override fun getCurrency(id: Int): CurrencyEntity? = mCached?.coins?.first { it.id == id }

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
            if (skipCache) {
                currenciesRequest()
            } else {
                mCached?.let {
                    Result.Success(it)
                } ?: currenciesRequest()
            }

    override suspend fun getWatchlist(skipCache: Boolean): Result<CurrencyDataResponse> =
            if (skipCache) {
                watchlistRequest()
            } else {
                mCached?.let {
                    Result.Success(it)
                } ?: watchlistRequest()
            }

    //endregion

    companion object {
        private const val NETWORK_PAGE_SIZE = 100
        private const val BD_PAGE_SIZE = 20
        private var INSTANCE: CurrencyRepository? = null

        fun getInstance(
                currencyClient: CurrencyClient,
                watchlist: WatchlistSourceContract
        ): CurrencySourceContract {
            if (INSTANCE == null) {
                INSTANCE = CurrencyRepository(currencyClient, watchlist)
            }
            return INSTANCE!!
        }
    }
}