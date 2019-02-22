package com.blocksdecoded.coinwave.view.watchlist

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity

interface WatchListContract {

    interface View : BaseMVPContract.View<Presenter> {

        fun showCoins(coins: List<CoinEntity>)

        fun showFavoriteCoin(coin: CoinEntity)

        fun showFavoriteChart(chartData: ChartData)

        fun showFavoriteLoading()

        fun hideFavoriteLoading()

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun showError(hideList: Boolean)

        fun showEmpty()

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