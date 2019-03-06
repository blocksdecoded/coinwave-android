package com.blocksdecoded.coinwave.presentation.addtowatchlist

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.core.mvp.BaseMVPContract

interface AddToWatchlistContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun hideLoadingError()

        fun showLoadingError()

        fun updateCoin(coinEntity: CoinEntity)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onCoinWatch(position: Int)

        fun getCoins()
    }
}