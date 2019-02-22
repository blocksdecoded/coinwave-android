package com.blocksdecoded.coinwave.view.pickfavorite

import com.blocksdecoded.core.mvp.BaseMVPPresenter
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.utils.coroutine.launchSilent
import com.blocksdecoded.utils.coroutine.model.onError
import com.blocksdecoded.utils.coroutine.model.onResult
import com.blocksdecoded.utils.coroutine.model.onSuccess
import com.blocksdecoded.utils.isValidIndex
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PickFavoritePresenter(
    view: PickFavoriteContract.View?,
    private val mFavoriteUseCases: FavoriteUseCases,
    private val mCoinsUseCases: CoinsUseCases,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : BaseMVPPresenter<PickFavoriteContract.View>(view), PickFavoriteContract.Presenter {

    private var mCachedData = arrayListOf<CoinEntity>()

    override fun attachView(view: PickFavoriteContract.View) {
        mView = view
        injectSelfToView()
    }

    //region Lifecycle

    override fun onResume() {
        super.onResume()
        getCoins()
    }

    //endregion

    //region Private

    private fun updateCache(coins: List<CoinEntity>) {
        mCachedData.clear()
        mCachedData.addAll(coins)
        mView?.showCoins(mCachedData)
    }

    private fun getCoins() = launchSilent(uiContext) {
        mView?.hideError()
        mView?.showLoading()
        mCoinsUseCases.getCoins(false)
                .onSuccess {
                    mView?.hideLoading()
                    updateCache(it)
                }
                .onError {
                    mView?.hideLoading()
                    mView?.showError()
                }
    }

    //endregion

    //region Contract

    override fun onCoinClick(position: Int) {
        if (mCachedData.isValidIndex(position)) {
            mFavoriteUseCases.setId(mCachedData[position].id)
            mView?.showMessage("Favorite saved.")
            mView?.finishView()
        }
    }

    override fun onRetryClick() {
        getCoins()
    }

    //endregion
}