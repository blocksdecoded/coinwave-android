package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.crypto.CryptoSourceContract
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.experimental.withContext

// Created by askar on 7/19/18.
class CurrencyListInteractor(
        private val appExecutors: AppExecutors,
        private val mCryptoService: CryptoSourceContract
): CurrencyListUseCases {
    override suspend fun getCryptoList(): Result<List<CurrencyEntity>> = withContext(appExecutors.networkContext) {
        val result = mCryptoService.getAllCurrencies()
        when(result){
            is Result.Success -> {
                Result.Success(result.data.data)
            }

            is Result.Error -> {
                Result.Error(result.exception)
            }
        }
    }
}