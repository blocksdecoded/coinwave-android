package com.blocksdecoded.coinwave.presentation.editwatchlist

import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.core.mvp.BaseMvpContract

interface EditWatchlistContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun hideLoadingError()

        fun showLoadingError()

        fun updateCoin(coinEntity: CoinEntity)
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onCoinWatch(position: Int)

        fun getCoins()
    }
}