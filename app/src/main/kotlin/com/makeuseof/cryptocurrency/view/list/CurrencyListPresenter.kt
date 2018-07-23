package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.utils.coroutine.launchSilent
import com.makeuseof.utils.isValidIndex
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

class CurrencyListPresenter(
        view: CurrencyListContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = UI
) : BaseMVPPresenter<CurrencyListContract.View>(view), CurrencyListContract.Presenter {
    private var mCachedData = listOf<CurrencyEntity>()

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
        val result = mCurrencyListUseCases.getCryptoList(true)
        when(result){
            is Result.Success -> {
                mCachedData = result.data
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
        if(mCachedData.isValidIndex(position)){
            if(mCachedData[position].isSaved){
                mCurrencyListUseCases.removeCurrency(mCachedData[position].id)
            } else {
                mCurrencyListUseCases.saveCurrency(mCachedData[position].id)
            }
        }
    }
}