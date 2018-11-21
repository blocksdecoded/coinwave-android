package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.core.mvp.BaseMVPPresenter
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.crypto.CurrencyUpdateObserver
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.cryptocurrency.util.addSortedByRank
import com.makeuseof.cryptocurrency.util.findCurrency
import com.makeuseof.utils.coroutine.launchSilent
import com.makeuseof.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class WatchListPresenter(
        view: WatchListContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<WatchListContract.View>(view), WatchListContract.Presenter {
    private var mCachedData = arrayListOf<CurrencyEntity>()

    private val mCurrenciesObserver = object: CurrencyUpdateObserver {
        override fun onAdded(currencyEntity: CurrencyEntity) = launchSilent(uiContext){
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }

        override fun onUpdated(currencies: List<CurrencyEntity>) = launchSilent(uiContext){
            setCache(currencies)
        }

        override fun onRemoved(currencyEntity: CurrencyEntity) = launchSilent(uiContext){
            mView?.deleteCurrency(removeCurrency(currencyEntity))
            if(mCachedData.isEmpty()) mView?.showEmpty()
        }
    }

    //region Private

    private fun setCache(currencies: List<CurrencyEntity>){
        mCachedData.clear()
        mCachedData.addAll(currencies.filter { it.isSaved })
        mView?.showCurrencies(mCachedData)

        if(mCachedData.isEmpty()) mView?.showEmpty()
    }

    private fun searchCurrency(currencyEntity: CurrencyEntity ,body: ((index: Int) -> Unit)? = null): Int =
            mCachedData.findCurrency(currencyEntity, body)

    private fun updateCurrency(currencyEntity: CurrencyEntity): Int {
        val index = searchCurrency(currencyEntity)

        if(index >= 0){

        } else {
            mCachedData.addSortedByRank(currencyEntity)
        }

        return 0
    }

    private fun removeCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData.removeAt(it)
            }

    private fun getCurrencies(skipCache: Boolean) = launchSilent(uiContext){
        mView?.showLoading()
        val result = mCurrencyListUseCases.getCryptoList(skipCache)
        when(result){
            is Result.Success -> {
                setCache(result.data)
            }

            is Result.Error -> {
                when(result.exception){
                    is NetworkException -> {
                        mView?.showNetworkError(mCachedData.isEmpty())
                    }
                }
            }
        }
    }

    //endregion

    //region Contract

    override fun attachView(view: WatchListContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCurrencyListUseCases.removeObserver(mCurrenciesObserver)
    }

    override fun onResume() {
        super.onResume()
        mCurrencyListUseCases.addObserver(mCurrenciesObserver)
        getCurrencies(false)
    }

    override fun getCurrencyList() {
        getCurrencies(true)
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