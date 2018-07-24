package com.makeuseof.cryptocurrency.data.crypto.network

import com.makeuseof.cryptocurrency.data.model.CurrencyListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Created by askar on 7/19/18.
interface CryptoNetworkClient {
    @GET(CryptoConfig.CURRENCIES_PATH)
    fun getCurrencies(
            @Query("limit") limit: Int = 20,
            @Query("structure") structure: String = "array"
    ): Call<CurrencyListResponse>
}