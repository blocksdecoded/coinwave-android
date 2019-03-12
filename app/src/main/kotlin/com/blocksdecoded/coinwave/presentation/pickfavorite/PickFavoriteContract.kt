package com.blocksdecoded.coinwave.presentation.pickfavorite

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.core.mvp.BaseMvpContract

interface PickFavoriteContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun showError()

        fun hideError()

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onRetryClick()
    }
}