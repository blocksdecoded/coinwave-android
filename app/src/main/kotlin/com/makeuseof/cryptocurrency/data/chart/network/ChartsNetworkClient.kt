package com.makeuseof.cryptocurrency.data.chart.network

import com.makeuseof.cryptocurrency.data.model.ChartData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Created by askar on 7/19/18.
interface ChartsNetworkClient {

    @GET("${ChartsConfig.CURRENCIES}/{currency}")
    fun getAllChartData(@Path("currency") currency: String): Call<ChartData>

    @GET("${ChartsConfig.CURRENCIES}/{currency}/{fromTime}/{toTime}")
    fun getChartForTime(
            @Path("currency") currency: String,
            @Path("fromTime") fromTime: Long,
            @Path("toTime") toTime: Long
    ): Call<ChartData>
}