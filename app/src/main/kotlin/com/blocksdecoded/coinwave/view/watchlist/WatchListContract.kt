package com.blocksdecoded.coinwave.view.watchlist

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

interface WatchListContract {

    interface View : BaseMVPContract.View<Presenter> {

        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun showFavoriteCurrency(currency: CurrencyEntity)

        fun showFavoriteChart(chartData: ChartData)

        fun showFavoriteLoading()

        fun hideFavoriteLoading()

        fun updateCurrency(position: Int, currencyEntity: CurrencyEntity)

        fun deleteCurrency(position: Int)

        fun showNetworkError(hideList: Boolean)

        fun openCurrencyScreen(id: Int)

        fun showDeleteConfirm(currencyEntity: CurrencyEntity, position: Int)

        fun showEmpty()

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {

        fun onCurrencyPick(position: Int)

        fun onCurrencyClick(position: Int)

        fun deleteCurrency(position: Int)

        fun getCurrencyList()

        fun onMenuClick()
    }
}