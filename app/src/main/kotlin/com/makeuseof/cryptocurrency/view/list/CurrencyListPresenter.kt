package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

class CurrencyListPresenter(
        view: CurrencyListContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = UI
) : BaseMVPPresenter<CurrencyListContract.View>(view), CurrencyListContract.Presenter {

    override fun attachView(view: CurrencyListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onResume() {
        super.onResume()
        getCurrencies()
    }

    private fun getCurrencies() = launchSilent(uiContext){
        mView?.showLoading()
        val result = mCurrencyListUseCases.getCryptoList()
        when(result){
            is Result.Success -> {
                mView?.showCurrencies(result.data)
            }

            is Result.Error -> {
                mView?.showNetworkError()
            }
        }
    }

    override fun getCurrencyList() {
        getCurrencies()
    }

    override fun onCurrencyPick(position: Int) {

    }

    override fun onCurrencyClick(position: Int) {

    }
}