package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.crypto.CurrencySourceContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.experimental.withContext

// Created by askar on 7/19/18.
class CurrencyListInteractor(
        private val appExecutors: AppExecutors,
        private val mCryptoService: CurrencySourceContract
): CurrencyListUseCases {
    override suspend fun getCryptoList(skipCache: Boolean): Result<List<CurrencyEntity>> = withContext(appExecutors.networkContext) {
        val result = mCryptoService.getAllCurrencies(skipCache)
        when(result){
            is Result.Success -> {
                Result.Success(result.data.currencies)
            }

            is Result.Error -> {
                Result.Error(result.exception)
            }
        }
    }

    override fun saveCurrency(id: Int): Boolean {
        return mCryptoService.saveCurrency(id)
    }

    override fun removeCurrency(id: Int): Boolean {
        return mCryptoService.removeCurrency(id)
    }
}