package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpContract

interface IWatchListContract {

    interface View : BaseMvpContract.View<Presenter> {

        fun showCoins(coins: List<CoinEntity>)

        fun showFavoriteCoin(coin: CoinEntity)

        fun showFavoriteChart(chartData: ChartData)

        fun updateCoin(position: Int, coinEntity: CoinEntity)

        fun deleteCoin(position: Int)

        fun openCoinInfo(id: Int)

        fun openAddToWatchlist()

        fun showError(hideList: Boolean)

        fun showEmpty()

        fun hideEmpty()

        fun showCoinsLoading()

        fun hideCoinsLoading()

        fun showFavoriteLoading()

        fun hideFavoriteLoading()

        fun showFavoriteError()

        fun hideFavoriteError()

        fun showSortType(sortType: CoinsCache.CoinSortEnum)
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun deleteCoin(position: Int)

        fun onAddCoinClick()

        fun getCoins()

        fun onMenuClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}