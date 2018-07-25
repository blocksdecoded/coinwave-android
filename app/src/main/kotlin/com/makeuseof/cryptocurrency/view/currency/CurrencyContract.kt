package com.makeuseof.cryptocurrency.view.currency

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.model.ChartData
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

interface CurrencyContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showChartData(chartData: ChartData)

        fun showCurrencyData(currencyEntity: CurrencyEntity)

        fun showChartLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun fetchCurrencyData(id: Int)
    }
}