package com.makeuseof.cryptocurrency.data.crypto

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.CurrencyListResponse

// Created by askar on 7/19/18.
interface CryptoSourceContract {
    suspend fun getAllCurrencies(): Result<CurrencyListResponse>
}