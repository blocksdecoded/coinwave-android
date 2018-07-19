package com.makeuseof.cryptocurrency.domain.usecases.list

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.model.CryptoEntity

// Created by askar on 7/19/18.
interface CryptoListUseCases {
    suspend fun getCryptoList(): Result<List<CryptoEntity>>
}