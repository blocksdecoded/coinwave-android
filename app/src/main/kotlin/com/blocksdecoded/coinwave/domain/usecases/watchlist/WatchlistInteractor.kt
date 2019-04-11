package com.blocksdecoded.coinwave.domain.usecases.watchlist

import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
import io.reactivex.Observable
import java.util.*

class WatchlistInteractor(
    private val mCoinsRepository: ICoinsRepository
) : IWatchlistUseCases {
    override val watchlistObservable: Observable<CoinsResult>
        get() = mCoinsRepository.coinsUpdateSubject
            .flatMap {
                Observable.just(
                    CoinsResult(
                        it.coins.filter { it.isSaved },
                        it.lastUpdated
                    )
                )
            }

    override fun getCoins(skipCache: Boolean, force: Boolean): Observable<CoinsResult> = mCoinsRepository.getAllCoins(skipCache, force)
        .map {
            CoinsResult(
                it.coins.filter { it.isSaved },
                it.updatedAt ?: Date()
            )
        }

    override fun getCoin(id: Int): CoinEntity? = mCoinsRepository.getCoin(id)
}