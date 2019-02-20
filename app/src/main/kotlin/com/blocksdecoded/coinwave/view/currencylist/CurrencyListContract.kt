package com.blocksdecoded.coinwave.view.currencylist

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.CurrencyEntity

interface CurrencyListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showCurrencies(currencies: List<CurrencyEntity>)

        fun updateCurrency(position: Int, currencyEntity: CurrencyEntity)

        fun deleteCurrency(position: Int)

        fun openCurrencyScreen(id: Int)

        fun showDeleteConfirm(currencyEntity: CurrencyEntity, position: Int)

        fun showNetworkError(hideList: Boolean)

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