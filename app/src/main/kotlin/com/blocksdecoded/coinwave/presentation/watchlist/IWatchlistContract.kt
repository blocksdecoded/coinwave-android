package com.blocksdecoded.coinwave.presentation.watchlist

import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.presentation.sort.CoinsCache
import com.blocksdecoded.coinwave.presentation.sort.ViewSortEnum
import com.blocksdecoded.core.mvp.BaseMvpContract

interface IWatchlistContract {

    interface View : BaseMvpContract.View<Presenter> {

        fun showCoins(coins: List<CoinEntity>)

        fun showFavoriteCoin(coin: CoinEntity)

        fun showFavoriteChart(chartData: ChartData)

        fun openCoinInfo(id: Int)

        fun openAddToWatchlist()

        fun showSortType(sortType: CoinsCache.CoinSortEnum)

        fun showError(hideList: Boolean)

        fun showList()

        fun hideList()

        fun showEmpty()

        fun hideEmpty()

        fun showCoinsLoading()

        fun hideCoinsLoading()

        fun showFavoriteLoading()

        fun hideFavoriteLoading()

        fun showFavoriteError()

        fun hideFavoriteError()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onCoinClick(position: Int)

        fun onAddCoinClick()

        fun getCoins()

        fun onMenuClick()

        fun onSortClick(sortType: ViewSortEnum)
    }
}