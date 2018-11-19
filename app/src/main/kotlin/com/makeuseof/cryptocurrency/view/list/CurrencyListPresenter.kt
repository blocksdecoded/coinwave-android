package com.makeuseof.cryptocurrency.view.list

import com.makeuseof.core.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.data.crypto.CurrencyUpdateObserver
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.cryptocurrency.util.findCurrency
import com.makeuseof.utils.coroutine.launchSilent
import com.makeuseof.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CurrencyListPresenter(
        view: CurrencyListContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<CurrencyListContract.View>(view), CurrencyListContract.Presenter {
    private var mCachedData = arrayListOf<CurrencyEntity>()
    private var mInitialized = false

    private val mCurrenciesObserver = object: CurrencyUpdateObserver {
        override fun onAdded(currencyEntity: CurrencyEntity) = launchSilent(uiContext){
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }

        override fun onUpdated(currencies: List<CurrencyEntity>) = launchSilent(uiContext){
            updateCache(currencies)
        }

        override fun onRemoved(currencyEntity: CurrencyEntity) = launchSilent(uiContext){
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }
    }

    //region Private

    private fun searchCurrency(currencyEntity: CurrencyEntity ,body: (index: Int) -> Unit): Int =
            mCachedData.findCurrency(currencyEntity, body)

    private fun updateCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData[it] = currencyEntity
            }

    private fun removeCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData.removeAt(it)
            }

    private fun updateCache(currencies: List<CurrencyEntity>){
        mCachedData.clear()
        mCachedData.addAll(currencies)
        mView?.showCurrencies(mCachedData)
    }

    private fun getCurrencies() = launchSilent(uiContext){
        mView?.showLoading()
        val result = mCurrencyListUseCases.getCryptoList(true)
        when(result){
            is Result.Success -> {
//                updateCache(result.data)
            }

            is Result.Error -> {
                mView?.showNetworkError(mCachedData.isEmpty())
            }
        }
    }

    //endregion

    //region Contract

    override fun onResume() {
        super.onResume()
        mCurrencyListUseCases.addObserver(mCurrenciesObserver)
        if (!mInitialized){
            mInitialized = true
            getCurrencies()
        }
    }

    override fun attachView(view: CurrencyListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCurrencyListUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun getCurrencyList() {
        getCurrencies()
    }

    override fun onCurrencyPick(position: Int) {

    }

    override fun deleteCurrency(position: Int) {
        if(mCachedData.isValidIndex(position)){
            if(mCachedData[position].isSaved){
                mView?.showMessage("${mCachedData[position].name} removed from Watchlist")
                mCurrencyListUseCases.removeCurrency(mCachedData[position].id)
            }
        }
    }

    override fun onCurrencyClick(position: Int) {
        if(mCachedData.isValidIndex(position)){
            mView?.openCurrencyScreen(mCachedData[position].id)
        }
    }

    //endregion
}