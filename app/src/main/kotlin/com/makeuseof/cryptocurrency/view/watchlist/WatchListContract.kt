package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface WatchListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun updateCurrency(position: Int, currencyEntity: CurrencyEntity)

        fun deleteCurrency(position: Int)

        fun showNetworkError(hideList: Boolean)

        fun showDeleteConfirm(currencyEntity: CurrencyEntity, position: Int)

        fun showEmpty()

        fun showLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyPick(position: Int)

        fun onCurrencyClick(position: Int)

        fun deleteCurrency(position: Int)

        fun getCurrencyList()
    }
}