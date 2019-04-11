package com.blocksdecoded.coinwave.domain.usecases.watchlist

import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
import com.blocksdecoded.coinwave.domain.usecases.BaseCoinsUseCase
import io.reactivex.Observable

interface IWatchlistUseCases : BaseCoinsUseCase {
    val watchlistObservable: Observable<CoinsResult>
}