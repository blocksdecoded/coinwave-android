package com.blocksdecoded.coinwave.data.chart.network

import com.blocksdecoded.coinwave.data.chart.network.model.ChartResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Created by askar on 7/19/18.
interface ChartsNetworkClient {
    @GET("${ChartsConfig.CHARTS_PATH}/{coin}/history/{period}")
    fun getChartForTime(
            @Path("coin") currency: String,
            @Path("period") period: String
    ): Call<ChartResponse>
}