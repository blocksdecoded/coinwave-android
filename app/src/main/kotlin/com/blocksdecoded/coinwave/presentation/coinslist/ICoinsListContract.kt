package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpContract

interface ICoinsListContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun showSortType(sortType: CoinsCache.CoinSortEnum)

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun showNetworkError(hideList: Boolean)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun getCoins()

        fun onMenuClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}