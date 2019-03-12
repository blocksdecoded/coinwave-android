package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClient
import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.watchlist.IWatchlistStorage
import io.reactivex.Flowable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

// Created by askar on 7/19/18.
class CoinsRepository(
        private val mCoinsClient: ICoinClient,
        private val mWatchlistSource: IWatchlistStorage,
        private val mLocalSource: ICoinsStorage?
) : ICoinsStorage {
    private var mCached: CoinsDataResponse? = null
    private val mObservers = hashSetOf<ICoinsObserver>()

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
            mLocalSource?.getAllCoins(true)
        }
    }

    //region Private

    //region Calls

    private fun coinsRequest() = mCoinsClient.getCoins(NETWORK_PAGE_SIZE)
        .doOnSuccess {
            setCoinsData(it.data)
            setCache(it.data)
        }
        .doOnError { mCached?.let { setCache(it) } }
        .map { it.data }
        .toFlowable()

    private fun watchlistRequest() =
            mCoinsClient.getCoins(NETWORK_PAGE_SIZE, watchlistIds)
                    .map { it.data }
                    .toFlowable()

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
        mLocalSource?.setCoinsData(coinsData)
    }

    override fun getCoin(id: Int): CoinEntity? = mCached?.coins?.first { it.id == id }

    override fun addCoinObserver(observer: ICoinsObserver) {
        mObservers.add(observer)
    }

    override fun removeCoinObserver(observer: ICoinsObserver) {
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

    override fun getAllCoins(skipCache: Boolean): Flowable<CoinsDataResponse> =
            if (skipCache) {
                coinsRequest()
            } else {
                mCached?.let {
                    Flowable.just(it)
                } ?: coinsRequest()
            }

    override fun getWatchlist(skipCache: Boolean): Flowable<CoinsDataResponse> =
            if (skipCache) {
                watchlistRequest()
            } else {
                mCached?.let {
                    Flowable.just(it)
                } ?: watchlistRequest()
            }

    //endregion

    companion object {
        private const val NETWORK_PAGE_SIZE = 100
        private const val BD_PAGE_SIZE = 20
    }
}