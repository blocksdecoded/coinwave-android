package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.crypto.remote.CoinClient
import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

// Created by askar on 7/19/18.
class CoinsRepository(
    private val mCoinsClient: CoinClient,
    private val mWatchlistSource: WatchlistSourceContract,
    private val mLocalSource: CoinsDataSource
) : CoinsDataSource {
    private var mCached: CoinsDataResponse? = null
    private val mObservers = hashSetOf<CoinsUpdateObserver>()

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

    init {
        GlobalScope.async {
            mLocalSource.getAllCoins(false)
                .onSuccess { mCached = it }
        }
    }

    //region Private

    //region Calls

    private suspend fun coinsRequest(): Result<CoinsDataResponse> {
        return mCoinsClient.getCoins(NETWORK_PAGE_SIZE)
            .onSuccess {
                setCoinsData(it.data)
                setCache(it.data)
            }
            .onError { mCached?.let { setCache(it) } }
            .mapOnSuccess { it.data }
    }

    private suspend fun watchlistRequest(): Result<CoinsDataResponse> =
            mCoinsClient.getCoins(NETWORK_PAGE_SIZE, watchlistIds)
                    .mapOnSuccess { it.data }

    //endregion

    private fun setCache(data: CoinsDataResponse) {
        markSaved(data.coins)
        mCached = data
        mObservers.forEach { it.onUpdated(data.coins) }
    }

    private fun markSaved(coins: List<CoinEntity>) {
        val saved = mWatchlistSource.getAll()
        coins.forEach {
            it.isSaved = saved.contains(it.id)
        }
    }

    private fun notifyAdded(coinEntity: CoinEntity) =
            mObservers.forEach { it.onAdded(coinEntity) }

    private fun notifyRemoved(coinEntity: CoinEntity) =
            mObservers.forEach { it.onRemoved(coinEntity) }

    private fun findCoin(id: Int, onFind: (coin: CoinEntity) -> Unit): Boolean =
            mCached?.coins?.first { it.id == id }?.let {
                onFind.invoke(it)
                true
            } ?: false

    //endregion

    //region Contract

    override fun setCoinsData(coinsData: CoinsDataResponse) {
        mLocalSource.setCoinsData(coinsData)
    }

    override fun getCoin(id: Int): CoinEntity? = mCached?.coins?.first { it.id == id }

    override fun addCoinObserver(observer: CoinsUpdateObserver) {
        mObservers.add(observer)
    }

    override fun removeCoinObserver(observer: CoinsUpdateObserver) {
        mObservers.remove(observer)
    }

    override fun saveCoin(id: Int): Boolean = findCoin(id) {
        mWatchlistSource.addId(id)
        it.isSaved = true
        notifyAdded(it)
    }

    override fun removeCoin(id: Int): Boolean = findCoin(id) {
        mWatchlistSource.deleteId(id)
        it.isSaved = false
        notifyRemoved(it)
    }

    override suspend fun getAllCoins(skipCache: Boolean): Result<CoinsDataResponse> =
            if (skipCache) {
                coinsRequest()
            } else {
                mCached?.let {
                    Result.Success(it)
                } ?: coinsRequest()
            }

    override suspend fun getWatchlist(skipCache: Boolean): Result<CoinsDataResponse> =
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
        private var INSTANCE: CoinsRepository? = null

        fun getInstance(
            coinClient: CoinClient,
            watchlist: WatchlistSourceContract,
            localSource: CoinsDataSource
        ): CoinsDataSource {
            if (INSTANCE == null) {
                INSTANCE = CoinsRepository(coinClient, watchlist, localSource)
            }
            return INSTANCE!!
        }
    }
}