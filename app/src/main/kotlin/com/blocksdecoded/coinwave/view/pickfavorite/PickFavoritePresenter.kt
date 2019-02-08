package com.blocksdecoded.coinwave.view.pickfavorite

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PickFavoritePresenter(
        view: PickFavoriteContract.View?,
        private val mFavoriteUseCases: FavoriteUseCases,
        private val mCurrencyListUseCases: CurrencyListUseCases,
        private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<PickFavoriteContract.View>(view), PickFavoriteContract.Presenter {

    private var mCachedData = arrayListOf<CurrencyEntity>()

    override fun attachView(view: PickFavoriteContract.View) {
        mView = view
        injectSelfToView()
    }

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCurrencies()
    }

    //endregion

    //region Private

    private fun updateCache(currencies: List<CurrencyEntity>){
        mCachedData.clear()
        mCachedData.addAll(currencies)
        mView?.showCurrencies(mCachedData)
    }

    private fun getCurrencies() = launchSilent(uiContext){
        mCurrencyListUseCases.getCryptoList(false)
                .onSuccess { updateCache(it) }
    }

    //endregion

    //region Contract

    override fun onCurrencyClick(position: Int) {
        if (mCachedData.isValidIndex(position)){
            mFavoriteUseCases.setId(mCachedData[position].id)
            mView?.showMessage("Favorite saved.")
            mView?.finishView()
        }
    }

    //endregion
}