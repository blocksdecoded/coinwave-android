package com.blocksdecoded.coinwave.presentation.coinslist

import com.blocksdecoded.core.mvp.deprecated.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum

interface CoinsListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun showNetworkError(hideList: Boolean)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCoinPick(position: Int)

        fun onCoinClick(position: Int)

        fun getCoins()

        fun onMenuClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}