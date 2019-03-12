package com.blocksdecoded.coinwave.presentation.pickfavorite

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpContract

interface IPickFavoriteContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun showSortType(sortType: CoinsCache.CoinSortEnum)

        fun showError()

        fun hideError()

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onRetryClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}