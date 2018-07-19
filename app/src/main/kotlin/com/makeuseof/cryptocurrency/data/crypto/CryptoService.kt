package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.core.model.Result
import com.makeuseof.core.network.NetworkClientFactory
import com.makeuseof.core.network.NetworkError
import com.makeuseof.core.network.RHWithErrorHandler
import com.makeuseof.cryptocurrency.data.NetworkException
import com.makeuseof.cryptocurrency.data.crypto.network.CryptoConfig
import com.makeuseof.cryptocurrency.data.crypto.network.CryptoNetworkClient
import com.makeuseof.cryptocurrency.data.model.CurrencyListResponse
import kotlin.coroutines.experimental.suspendCoroutine

// Created by askar on 7/19/18.
class CryptoService: CryptoSourceContract {

    private val mClient: CryptoNetworkClient = NetworkClientFactory.getRetrofitClient(
            CryptoNetworkClient::class.java,
            CryptoConfig.BASE_URL
    )

    companion object {
        private var INSTANCE: CryptoService? = null

        fun getInstance(): CryptoSourceContract{
            if (INSTANCE == null){
                INSTANCE = CryptoService()
            }
            return INSTANCE!!
        }
    }

    //region Contract

    override suspend fun getAllCurrencies(): Result<CurrencyListResponse> = suspendCoroutine {
        val call = mClient.getCurrencies()
        call.enqueue(object : RHWithErrorHandler<CurrencyListResponse>{
            override fun onSuccess(result: CurrencyListResponse) {
                it.resume(Result.Success(result))
            }

            override fun onFailure(error: NetworkError) {
                it.resume(Result.Error(NetworkException(error.toString())))
            }
        })
    }

    //endregion
}