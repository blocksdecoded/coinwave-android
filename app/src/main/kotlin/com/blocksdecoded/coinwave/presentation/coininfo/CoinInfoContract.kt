package com.blocksdecoded.coinwave.presentation.coininfo

import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.core.mvp.BaseMvpContract

interface CoinInfoContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showChartData(chartData: ChartData)

        fun showCurrencyData(coinEntity: CoinEntity)

        fun setWatched(watched: Boolean)

        fun openSite(url: String)

        fun showLoading()

        fun hideLoading()

        fun showChartError()

        fun hideChartError()
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun fetchCurrencyData(id: Int)

        fun onPeriodChanged(position: Int)

        fun onGoToWebsiteClick()

        fun onWatchingClick()
    }
}