package com.blocksdecoded.coinwave.view.coinslist

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CoinEntity

interface CoinsListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCoins(coins: List<CoinEntity>)

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun showDeleteConfirm(coinEntity: CoinEntity, position: Int)

        fun showNetworkError(hideList: Boolean)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCoinPick(position: Int)

        fun onCoinClick(position: Int)

        fun deleteCoin(position: Int)

        fun getCoins()

        fun onMenuClick()
    }
}