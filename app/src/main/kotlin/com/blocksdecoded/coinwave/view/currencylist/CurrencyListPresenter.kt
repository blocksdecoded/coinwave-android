package com.blocksdecoded.coinwave.view.currencylist

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.crypto.CurrencyUpdateObserver
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.coinwave.util.findCurrency
import com.blocksdecoded.coinwave.view.main.MenuClickListener
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CurrencyListPresenter(
    view: CurrencyListContract.View?,
    private val mMenuListener: MenuClickListener,
    private val mCurrencyListUseCases: CurrencyListUseCases,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<CurrencyListContract.View>(view), CurrencyListContract.Presenter {
    private var mCachedData = arrayListOf<CurrencyEntity>()
    private var mInitialized = false

    private val mCurrenciesObserver = object : CurrencyUpdateObserver {
        override fun onAdded(currencyEntity: CurrencyEntity) = launchSilent(uiContext) {
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }

        override fun onUpdated(currencies: List<CurrencyEntity>) = launchSilent(uiContext) {
            updateCache(currencies)
        }

        override fun onRemoved(currencyEntity: CurrencyEntity) = launchSilent(uiContext) {
            mView?.updateCurrency(updateCurrency(currencyEntity), currencyEntity)
        }
    }

    //region Private

    private fun searchCurrency(currencyEntity: CurrencyEntity, body: (index: Int) -> Unit): Int =
            mCachedData.findCurrency(currencyEntity, body)

    private fun updateCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData[it] = currencyEntity
            }

    private fun removeCurrency(currencyEntity: CurrencyEntity): Int =
            searchCurrency(currencyEntity) {
                mCachedData.removeAt(it)
            }

    private fun updateCache(currencies: List<CurrencyEntity>) {
        mCachedData.clear()
        mCachedData.addAll(currencies)
        mView?.showCurrencies(mCachedData)
    }

    private fun getCurrencies() = launchSilent(uiContext) {
        mView?.showLoading()
        mCurrencyListUseCases.getCryptoList(true)
                .onSuccess { mView?.hideLoading() }
                .onError {
                    mView?.hideLoading()
                    mView?.showNetworkError(mCachedData.isEmpty())
                }
    }

    //endregion

    //region Contract

    override fun onResume() {
        super.onResume()
        mCurrencyListUseCases.addObserver(mCurrenciesObserver)
        if (!mInitialized) {
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
        if (mCachedData.isValidIndex(position)) {
            if (mCachedData[position].isSaved) {
                mView?.showMessage("${mCachedData[position].name} removed from Watchlist")
                mCurrencyListUseCases.removeCurrency(mCachedData[position].id)
            }
        }
    }

    override fun onCurrencyClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            mView?.openCurrencyScreen(mCachedData[position].id)
        }
    }

    override fun onMenuClick() {
        mMenuListener.onMenuClick()
    }

    //endregion
}