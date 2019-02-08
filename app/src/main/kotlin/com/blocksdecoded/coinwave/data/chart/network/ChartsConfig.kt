package com.blocksdecoded.coinwave.data.chart.network

import com.blocksdecoded.coinwave.BuildConfig

// Created by askar on 7/19/18.
object ChartsConfig {
    const val BASE_URL = BuildConfig.API_CURRENCY

    const val API_VERSION = "/v1"
    const val PUBLIC = "/public"
    const val COIN_PATH = "/coin"
    const val FIELD_COIN_ID = "{coin}"
    const val HISTORY = "/history"
    const val FIELD_PERIOD = "{period}"
    const val CHARTS_PATH = "$API_VERSION$PUBLIC$COIN_PATH"
}