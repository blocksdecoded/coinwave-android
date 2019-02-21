package com.blocksdecoded.coinwave.view.currency

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases.ChartPeriod.*
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CurrencyPresenter(
    view: CurrencyContract.View?,
    private val mChartsUseCases: ChartsUseCases,
    private val mCurrencyListUseCases: CurrencyListUseCases,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<CurrencyContract.View>(view), CurrencyContract.Presenter {
    private var mCached: CurrencyEntity? = null

    override fun onGoToWebsiteClick() {
        mCached?.let {
            mView?.openSite(it.websiteSlug)
        }
    }

    override fun attachView(view: CurrencyContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun fetchCurrencyData(id: Int) = launchSilent(uiContext) {
        mCached = mCurrencyListUseCases.getCurrency(id)
        mCached?.let {
            mView?.showCurrencyData(it)
        }
        mView?.showChartLoading()
        mChartsUseCases.getChartData(id)
                .onSuccess { mView?.showChartData(it) }
                .onError { mView?.showMessage("Chart load error") }
    }

    override fun onPeriodChanged(position: Int) = launchSilent(uiContext) {
        mCached?.let {
            val period = when (position) {
                0 -> TODAY
                1 -> WEEK
                2 -> MONTH_1
                3 -> YEAR
                4 -> ALL
                else -> TODAY
            }
            mView?.showChartLoading()
            mChartsUseCases.getChartData(it.id, period)
                    .onSuccess { mView?.showChartData(it) }
                    .onError { mView?.showMessage("Chart load error") }
        }
    }

    override fun onWatchingClick() {
        mCached?.let {
            mView?.setWatched(it.isSaved.not())
            if (it.isSaved) {
                mCurrencyListUseCases.removeCurrency(it.id)
                it.isSaved = false
            } else {
                mCurrencyListUseCases.saveCurrency(it.id)
                it.isSaved = true
            }
        }
    }
}