package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.core.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.data.crypto.CurrencyUpdateObserver
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.utils.coroutine.launchSilent
import com.makeuseof.utils.isValidIndex
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

class WatchListPresenter(
        view: WatchListContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = UI
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    private var mCachedData = listOf<CurrencyEntity>()

    private val mCurrenciesObserver = object: CurrencyUpdateObserver {
        override fun onAdded(currencyEntity: CurrencyEntity) {
            mView?.updateCurrency(currencyEntity)
        }

        override fun onUpdated(currencies: List<CurrencyEntity>) {
            mCachedData = currencies.filter { it.isSaved }
            mView?.showCurrencies(mCachedData)
        }

        override fun onRemoved(currencyEntity: CurrencyEntity) {
            mView?.deleteCurrency(currencyEntity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCurrencyListUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun attachView(view: WatchListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onResume() {
        super.onResume()
        mCurrencyListUseCases.addObserver(mCurrenciesObserver)
        getCurrencies()
    }

    private fun getCurrencies() = launchSilent(uiContext){
        mView?.showLoading()
        val result = mCurrencyListUseCases.getCryptoList(false)
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
        if(mCachedData.isValidIndex(position)){
            if(mCachedData[position].isSaved){
                mCurrencyListUseCases.removeCurrency(mCachedData[position].id)
            } else {
                mCurrencyListUseCases.saveCurrency(mCachedData[position].id)
            }
        }
    }
}