package com.blocksdecoded.coinwave.domain.usecases.watchlist

import com.blocksdecoded.coinwave.data.coins.ICoinsStorage
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import io.reactivex.Observable
import java.util.*

class WatchlistInteractor(
    private val mCoinsStorage: ICoinsStorage
) : IWatchlistUseCases {
    override val watchlistObservable: Observable<CoinsResult>
        get() = mCoinsStorage.coinsUpdateSubject
            .flatMap {
                Observable.just(CoinsResult(it.coins.filter { it.isSaved }, it.lastUpdated))
            }

    override fun getCoins(skipCache: Boolean, force: Boolean): Observable<CoinsResult> = mCoinsStorage.getAllCoins(skipCache, force)
        .map { CoinsResult(it.coins.filter { it.isSaved }, it.updatedAt ?: Date()) }

    override fun getCoin(id: Int): CoinEntity? = mCoinsStorage.getCoin(id)
}