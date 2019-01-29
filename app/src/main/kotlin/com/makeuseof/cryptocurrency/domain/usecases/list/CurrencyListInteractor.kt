package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.utils.coroutine.model.mapOnSuccess
import com.makeuseof.cryptocurrency.data.crypto.CurrencySourceContract
import com.makeuseof.cryptocurrency.data.crypto.CurrencyUpdateObserver
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.withContext

// Created by askar on 7/19/18.
class CurrencyListInteractor(
        private val appExecutors: AppExecutors,
        private val mCryptoService: CurrencySourceContract
): CurrencyListUseCases {
    override suspend fun getCryptoList(skipCache: Boolean): Result<List<CurrencyEntity>> = withContext(appExecutors.ioContext) {
        mCryptoService.getAllCurrencies(skipCache)
                .mapOnSuccess { it.coins }
    }

    override fun getCurrency(id: Int): CurrencyEntity? {
        return mCryptoService.getCurrency(id)
    }

    override fun saveCurrency(id: Int): Boolean {
        return mCryptoService.saveCurrency(id)
    }

    override fun removeCurrency(id: Int): Boolean {
        return mCryptoService.removeCurrency(id)
    }

    override fun addObserver(observer: CurrencyUpdateObserver) {
        mCryptoService.addCurrencyObserver(observer)
    }

    override fun removeObserver(observer: CurrencyUpdateObserver) {
        mCryptoService.removeCurrencyObserver(observer)
    }
}