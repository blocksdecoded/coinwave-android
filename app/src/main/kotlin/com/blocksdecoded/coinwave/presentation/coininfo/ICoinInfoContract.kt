package com.blocksdecoded.coinwave.presentation.coininfo

import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.core.mvp.BaseMvpContract

interface ICoinInfoContract {

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