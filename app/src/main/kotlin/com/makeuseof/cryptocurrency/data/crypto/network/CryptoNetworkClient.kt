package com.makeuseof.cryptocurrency.data.crypto.network

import com.makeuseof.cryptocurrency.data.model.CryptoListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Created by askar on 7/19/18.
interface CryptoNetworkClient {
    @GET(CryptoConfig.CURRENCIES_PATH)
    fun getCurrencies(@Query("structure") structure: String = "array"): Call<CryptoListResponse>
}