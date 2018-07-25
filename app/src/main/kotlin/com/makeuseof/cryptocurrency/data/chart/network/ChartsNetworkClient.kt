package com.makeuseof.cryptocurrency.data.chart.network

import com.makeuseof.cryptocurrency.data.model.ChartData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Created by askar on 7/19/18.
interface ChartsNetworkClient {
    @GET("${ChartsConfig.CURRENCIES}/{currency}")
    fun getChart(@Path("currency") currency: String): Call<ChartData>
}