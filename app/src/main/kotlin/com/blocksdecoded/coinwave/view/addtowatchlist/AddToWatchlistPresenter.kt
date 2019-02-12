package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AddToWatchlistPresenter(
        view: AddToWatchlistContract.View?,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = Dispatchers.Main
 ) : BaseMVPPresenter<AddToWatchlistContract.View>(view), AddToWatchlistContract.Presenter {
    override fun attachView(view: AddToWatchlistContract.View) {
        mView = view
        injectSelfToView()
    }

    override fun onResume() = launchSilent(uiContext) {
        super.onResume()
        mCurrencyListUseCases.getCryptoList(false)
                .onSuccess { mView?.showCurrencies(it) }
    }

    //region Contract

    override fun onCurrencyClick(position: Int) {

    }

    override fun onCurrencyWatch(position: Int) {

    }

    //endregion
}