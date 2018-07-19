package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.NetworkError
import com.makeuseof.cryptocurrency.data.model.CryptoListResponse
import com.makeuseof.utils.coroutine.AppExecutors
import kotlinx.coroutines.experimental.withContext

// Created by askar on 7/19/18.
class CryptoService(
        private val appExecutors: AppExecutors
): CryptoSourceContract {
    companion object {
        private var INSTANCE: CryptoService? = null

        fun getInstance(appExecutors: AppExecutors): CryptoSourceContract{
            if (INSTANCE == null){
                INSTANCE = CryptoService(appExecutors)
            }
            return INSTANCE!!
        }
    }

    //region Contract

    override suspend fun getAllCurrencies(): Result<CryptoListResponse> = withContext(appExecutors.ioContext) {
        Result.Error(NetworkError())
    }

    //endregion
}