package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AddToWatchlistPresenter(
    view: AddToWatchlistContract.View?,
    private val mCurrencyListUseCases: CurrencyListUseCases,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<AddToWatchlistContract.View>(view), AddToWatchlistContract.Presenter {

    private var cached = arrayListOf<CurrencyEntity>()

    override fun attachView(view: AddToWatchlistContract.View) {
        mView = view
        injectSelfToView()
    }

    //region Lifecycle

    override fun onResume() = launchSilent(uiContext) {
        super.onResume()
        mCurrencyListUseCases.getCryptoList(false).onSuccess(::updateCache)
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CurrencyEntity>) {
        cached.clear()
        cached.addAll(coins)
        mView?.showCurrencies(coins)
    }

    private fun updateCurrencyWatch(position: Int) {
        if (cached.isValidIndex(position)) {
            cached[position].isSaved = !cached[position].isSaved

            if (cached[position].isSaved) {
                mCurrencyListUseCases.saveCurrency(cached[position].id)
            } else {
                mCurrencyListUseCases.removeCurrency(cached[position].id)
            }

            mView?.updateCurrency(cached[position])
        }
    }

    //endregion

    //region Contract

    override fun getCurrencies() {

    }

    override fun onCurrencyClick(position: Int) {
        updateCurrencyWatch(position)
    }

    override fun onCurrencyWatch(position: Int) {
        updateCurrencyWatch(position)
    }

    //endregion
}