package com.blocksdecoded.coinwave.data.crypto

import com.blocksdecoded.coinwave.data.crypto.local.CoinsLocalStorage
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClient
import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.data.watchlist.IWatchlistStorage
import io.reactivex.Observable
import java.util.*

// Created by askar on 7/19/18.
class CoinsRepository(
    private val mCoinsClient: ICoinClient,
    private val mWatchlistSource: IWatchlistStorage,
    private val mLocalSource: CoinsLocalStorage
) : ICoinsStorage {
    private var mCached: CoinsDataResponse? = null
        set(value) {
            field = value
            field?.coins?.let { markSaved(it) }
        }
    private val mObservers = hashSetOf<ICoinsObserver>()

    init {
        mLocalSource.getAllCoins().subscribe { mCached = it }
    }

    //region Private

    //region Calls

    private fun isDirty(): Boolean = mCached?.updatedAt
            ?.let { (Date().time - it.time) > VALID_CACHE_TIME } ?: true

    private fun localCoinsFetch() = if (mCached != null)
        Observable.just(mCached!!)
    else
        mLocalSource.getAllCoins()

    private fun remoteCoinsFetch(force: Boolean) = if (isDirty() || force) mCoinsClient.getCoins(NETWORK_PAGE_SIZE)
            .doOnNext {
                it.data.updatedAt = Date()
                setCoinsData(it.data)
                setCache(it.data)
            }
            .doOnError { mCached?.let { setCache(it) } }
            .map { it.data } else Observable.empty()

    private fun fetchCoins(force: Boolean) = Observable.concat(localCoinsFetch(), remoteCoinsFetch(force))

    //endregion

    private fun setCache(data: CoinsDataResponse) {
        mCached = data
        mObservers.forEach { it.onUpdated(CoinsResult(data.coins, data.updatedAt ?: Date())) }
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

    override fun getAllCoins(skipCache: Boolean, force: Boolean): Observable<CoinsDataResponse> =
            if (skipCache) {
                fetchCoins(force)
            } else {
                mCached?.let {
                    Observable.just(it)
                } ?: fetchCoins(false)
            }

    override fun getWatchlist(skipCache: Boolean): Observable<CoinsDataResponse> =
            if (skipCache) {
                fetchCoins(false)
            } else {
                mCached?.let {
                    Observable.just(it)
                } ?: fetchCoins(false)
            }

    //endregion

    companion object {
        private const val VALID_CACHE_TIME = 5 * 60 * 1000
        private const val NETWORK_PAGE_SIZE = 50
    }
}