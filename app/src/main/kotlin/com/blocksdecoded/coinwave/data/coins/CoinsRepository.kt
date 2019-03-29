package com.blocksdecoded.coinwave.data.coins

import com.blocksdecoded.coinwave.data.coins.local.CoinsLocalStorage
import com.blocksdecoded.coinwave.data.coins.remote.ICoinClient
import com.blocksdecoded.coinwave.data.model.*
import com.blocksdecoded.coinwave.data.watchlist.IWatchlistStorage
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
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

    override val coinsUpdateSubject: PublishSubject<CoinsResult> = PublishSubject.create()

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
            .toObservable()
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
        coinsUpdateSubject.onNext(CoinsResult(data.coins, data.updatedAt ?: Date()))
    }

    private fun markSaved(coins: List<CoinEntity>) {
        val saved = mWatchlistSource.getAll()
        coins.forEach {
            it.isSaved = saved.contains(it.id)
        }
    }

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

    override fun saveCoin(id: Int): Boolean = findCoin(id) {
        mWatchlistSource.addId(id)
        it.isSaved = true
    }

    override fun removeCoin(id: Int): Boolean = findCoin(id) {
        mWatchlistSource.deleteId(id)
        it.isSaved = false
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

    override fun getChart(chartName: String, period: ChartPeriodEnum): Single<ChartData> =
        mCoinsClient.getHistory(chartName, period)
            .map { ChartData(it.data.history) }

    //endregion

    companion object {
        private const val VALID_CACHE_TIME = 5 * 60 * 1000
        private const val NETWORK_PAGE_SIZE = 50
    }
}