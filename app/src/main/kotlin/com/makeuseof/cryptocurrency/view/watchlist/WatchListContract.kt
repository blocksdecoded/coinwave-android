package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface WatchListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun updateCurrency(currency: CurrencyEntity)

        fun deleteCurrency(currency: CurrencyEntity)

        fun showNetworkError()

        fun showLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyPick(position: Int)

        fun onCurrencyClick(position: Int)

        fun getCurrencyList()
    }
}