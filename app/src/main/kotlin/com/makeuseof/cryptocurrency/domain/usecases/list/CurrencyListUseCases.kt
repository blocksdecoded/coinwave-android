package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity

// Created by askar on 7/19/18.
interface CurrencyListUseCases {
    suspend fun getCryptoList(): Result<List<CurrencyEntity>>
}