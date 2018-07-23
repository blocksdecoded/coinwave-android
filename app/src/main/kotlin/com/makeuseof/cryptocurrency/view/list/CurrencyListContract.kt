package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface CurrencyListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun updateCurrency(position: Int, currencyEntity: CurrencyEntity)

        fun deleteCurrency(position: Int)

        fun showNetworkError()

        fun showLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyPick(position: Int)

        fun onCurrencyClick(position: Int)

        fun getCurrencyList()
    }
}