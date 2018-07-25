package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface CurrencyListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun updateCurrency(position: Int, currencyEntity: CurrencyEntity)

        fun deleteCurrency(position: Int)

        fun showDeleteConfirm(currencyEntity: CurrencyEntity, position: Int)

        fun showNetworkError(hideList: Boolean)

        fun showLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onCurrencyPick(position: Int)

        fun onCurrencyClick(position: Int)

        fun deleteCurrency(position: Int)

        fun getCurrencyList()
    }
}