package com.blocksdecoded.coinwave.data.coins.local

import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.shared.ISharedStorage
import com.google.gson.Gson
import io.reactivex.Observable

class CoinsLocalStorage(
    private val mSharedStorage: ISharedStorage
) {
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

    fun setCoinsData(coinsData: CoinsDataResponse) = saveCoinsData(coinsData)

    fun getAllCoins(): Observable<CoinsDataResponse> =
        fetchSaveCoins()?.let { Observable.just(it) } ?: Observable.empty()

    //endregion

    companion object {
        private const val PREF_COINS_KEY = "coins"
    }
}