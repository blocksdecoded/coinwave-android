package com.blocksdecoded.coinwave.view.coininfo

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.CoinEntity

interface CoinInfoContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showChartData(chartData: ChartData)

        fun showCurrencyData(coinEntity: CoinEntity)

        fun setWatched(watched: Boolean)

        fun openSite(url: String)

        fun showChartLoading()
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun fetchCurrencyData(id: Int)

        fun onPeriodChanged(position: Int)

        fun onGoToWebsiteClick()

        fun onWatchingClick()
    }
}