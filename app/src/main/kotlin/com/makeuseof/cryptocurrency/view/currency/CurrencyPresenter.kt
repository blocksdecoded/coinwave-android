package com.makeuseof.cryptocurrency.view.currency

import com.makeuseof.core.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsUseCases
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

class CurrencyPresenter(
        view: CurrencyContract.View?,
        private val mChartsUseCases: ChartsUseCases,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = UI
) : BaseMVPPresenter<CurrencyContract.View>(view), CurrencyContract.Presenter {

    private var mCached: CurrencyEntity? = null

    override fun attachView(view: CurrencyContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun fetchCurrencyData(id: Int) = launchSilent(uiContext){
        mCached = mCurrencyListUseCases.getCurrency(id)
        mCached?.let {
            mView?.showCurrencyData(it)
        }
        mView?.showChartLoading()
        val result = mChartsUseCases.getChartData(id)
        when(result){
            is Result.Success -> {
                mView?.showChartData(result.data)
            }
            is Result.Error -> {
                mView?.showMessage("Chart load error")
            }
        }
    }
}