package com.blocksdecoded.coinwave.data.crypto.local

import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.ICoinsObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.shared.ISharedStorage
import com.google.gson.Gson
import io.reactivex.Flowable
import java.util.concurrent.Executors

class CoinsLocalStorage(
    private val mSharedStorage: ISharedStorage
) : ICoinsStorage {
    val executor = Executors.newSingleThreadExecutor()

    private fun fetchSaveCoins(): CoinsDataResponse? = try {
        Gson().fromJson(
            mSharedStorage.getPreference(PREF_COINS_KEY, ""),
            CoinsDataResponse::class.java
        )
    } catch (e: Exception) {
        logE(e)
        null
    }

    private fun saveCoinsData(coinsData: CoinsDataResponse) = try {
        mSharedStorage.setPreference(PREF_COINS_KEY, Gson().toJson(coinsData))
    } catch (e: Exception) {
        logE(e)
    }

    //region Contract

    override fun setCoinsData(coinsData: CoinsDataResponse) = saveCoinsData(coinsData)

    override fun getAllCoins(skipCache: Boolean): Flowable<CoinsDataResponse> {
        return fetchSaveCoins()?.let { Flowable.just(it) } ?: Flowable.error(Exception("Local data source not support watchlist fetch."))
    }

    override fun getWatchlist(skipCache: Boolean): Flowable<CoinsDataResponse> =
        Flowable.error(Exception("Local data source not support watchlist fetch."))

    override fun getCoin(id: Int): CoinEntity? = null

    override fun saveCoin(id: Int): Boolean = false

    override fun removeCoin(id: Int): Boolean = false

    override fun addCoinObserver(observer: ICoinsObserver) = Unit

    override fun removeCoinObserver(observer: ICoinsObserver) = Unit

    //endregion

    companion object {
        private const val PREF_COINS_KEY = "coins"
    }
}