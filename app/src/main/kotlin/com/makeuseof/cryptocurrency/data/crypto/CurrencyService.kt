package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.core.model.Result
import com.makeuseof.core.network.NetworkClientFactory
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.EmptyCache
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.crypto.network.CryptoConfig
import com.makeuseof.cryptocurrency.data.crypto.network.CryptoNetworkClient
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.cryptocurrency.data.model.CurrencyListResponse
import com.makeuseof.cryptocurrency.data.watchlist.WatchlistSourceContract
import kotlin.coroutines.experimental.suspendCoroutine

// Created by askar on 7/19/18.
class CurrencyService(
        private val mWatchlistSource: WatchlistSourceContract
): CurrencySourceContract {
    private var mCached: CurrencyListResponse? = null
    private val mObservers = hashSetOf<CurrencyUpdateObserver>()

    private val mClient: CryptoNetworkClient = NetworkClientFactory.getRetrofitClient(
            CryptoNetworkClient::class.java,
            CryptoConfig.BASE_URL
    )

    companion object {
        private var INSTANCE: CurrencyService? = null

        fun getInstance(watchlist: WatchlistSourceContract): CurrencySourceContract{
            if (INSTANCE == null){
                INSTANCE = CurrencyService(watchlist)
            }
            return INSTANCE!!
        }
    }

    //region Private

    private fun setCache(data: CurrencyListResponse){
        markSaved(data.currencies)
        mCached = data
        mObservers.forEach { it.onUpdated(data.currencies) }
    }

    private fun markSaved(currencies: List<CurrencyEntity>){
        val saved = mWatchlistSource.getAll()
        currencies.forEach {
            it.isSaved = saved.contains(it.id)
        }
    }

    private fun notifyAdded(currencyEntity: CurrencyEntity){
        mObservers.forEach { it.onAdded(currencyEntity) }
    }

    private fun notifyRemoved(currencyEntity: CurrencyEntity){
        mObservers.forEach { it.onRemoved(currencyEntity) }
    }

    private fun findCurrency(id: Int, onFind: (currency: CurrencyEntity) -> Unit): Boolean{
        val currency = mCached?.currencies?.first { it.id == id }
        return if(currency != null){
            onFind.invoke(currency)
            true
        } else {
            false
        }
    }

    //endregion

    //region Contract

    override fun getCurrency(id: Int): CurrencyEntity? {
        return mCached?.currencies?.get(id)
    }

    override fun addCurrencyObserver(observer: CurrencyUpdateObserver) {
        mObservers.add(observer)
    }

    override fun removeCurrencyObserver(observer: CurrencyUpdateObserver) {
        mObservers.remove(observer)
    }

    override fun saveCurrency(id: Int): Boolean = findCurrency(id) {
        mWatchlistSource.addId(id)
        it.isSaved = true
        notifyAdded(it)
    }

    override fun removeCurrency(id: Int): Boolean = findCurrency(id) {
        mWatchlistSource.deleteId(id)
        it.isSaved = false
        notifyRemoved(it)
    }

    override suspend fun getAllCurrencies(skipCache: Boolean): Result<CurrencyListResponse> = suspendCoroutine {
        if (skipCache){
            val call = mClient.getCurrencies()
            call.enqueue(object : RHWithErrorHandler<CurrencyListResponse>{
                override fun onSuccess(result: CurrencyListResponse) {
                    setCache(result)
                    it.resume(Result.Success(result))
                }

                override fun onFailure(error: NetworkError) {
                    it.resume(Result.Error(NetworkException(error.toString())))
                    if (mCached != null){
                        setCache(mCached!!)
                    }
                }
            })
        } else {
            if (mCached != null){
                it.resume(Result.Success(mCached!!))
            } else {
                it.resume(Result.Error(EmptyCache("Cache is empty")))
            }
        }
    }

    //endregion
}