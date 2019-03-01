package com.blocksdecoded.coinwave.data.crypto.local

import com.blocksdecoded.coinwave.data.crypto.CoinsDataSource
import com.blocksdecoded.coinwave.data.crypto.CoinsUpdateObserver
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsDataResponse
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.shared.SharedContract
import com.google.gson.Gson

class CoinsLocalDataSource(
    private val mSharedStorage: SharedContract
) : CoinsDataSource {

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

    override suspend fun getAllCoins(skipCache: Boolean): Result<CoinsDataResponse> =
        fetchSaveCoins()?.let { Result.Success(it) } ?: Result.Error(Exception())

    override suspend fun getWatchlist(skipCache: Boolean): Result<CoinsDataResponse> {
        return Result.Error(Exception("Local data source not support watchlist fetch."))
    }

    override fun getCoin(id: Int): CoinEntity? = null

    override fun saveCoin(id: Int): Boolean = false

    override fun removeCoin(id: Int): Boolean = false

    override fun addCoinObserver(observer: CoinsUpdateObserver) = Unit

    override fun removeCoinObserver(observer: CoinsUpdateObserver) = Unit

    //endregion

    companion object {
        private val PREF_COINS_KEY = "coins"

    }
}