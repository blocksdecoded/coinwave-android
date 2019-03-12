package com.blocksdecoded.coinwave.presentation.pickfavorite

import com.blocksdecoded.core.mvp.deprecated.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CoinEntity

interface PickFavoriteContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun showError()

        fun hideError()

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onRetryClick()
    }
}