package com.blocksdecoded.coinwave.presentation.coininfo

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.coroutines.launch

class CoinInfoPresenter(
    view: CoinInfoContract.View?,
    private val mChartsUseCases: ChartsUseCases,
    private val mCoinsUseCases: CoinsUseCases
) : BaseMVPPresenter<CoinInfoContract.View>(view), CoinInfoContract.Presenter {
    private var mCached: CoinEntity? = null

    private fun fetchChartData(id: Int, periodEnum: ChartsUseCases.ChartPeriod = TODAY) {
        mView?.hideChartError()
        mView?.showLoading()
        disposables.add(
            mChartsUseCases.getChartData(id, periodEnum)
                .doAfterTerminate { scope.launch { mView?.hideLoading() }  }
                .uiSubscribe(
                    { chartData -> mView?.showChartData(chartData) },
                    { error -> mView?.showChartError() }))
    }

    override fun onGoToWebsiteClick() {
        mCached?.let {
            mView?.openSite(it.websiteSlug)
        }
    }

    override fun attachView(view: CoinInfoContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun fetchCurrencyData(id: Int) = launchSilent(scope) {
        mCached = mCoinsUseCases.getCoin(id)
        mCached?.let {
            mView?.showCurrencyData(it)

            fetchChartData(id)
        }
    }

    override fun onPeriodChanged(position: Int) = launchSilent(scope) {
        mCached?.let {
            val period = when (position) {
                0 -> TODAY
                1 -> WEEK
                2 -> MONTH_1
                3 -> YEAR
                4 -> ALL
                else -> TODAY
            }

            fetchChartData(it.id, period)
        }
    }

    override fun onWatchingClick() {
        mCached?.let {
            mView?.setWatched(it.isSaved.not())
            if (it.isSaved) {
                mCoinsUseCases.removeCoin(it.id)
                it.isSaved = false
            } else {
                mCoinsUseCases.saveCoin(it.id)
                it.isSaved = true
            }
        }
    }
}