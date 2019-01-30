package com.makeuseof.cryptocurrency.data.crypto.network

import com.makeuseof.cryptocurrency.BuildConfig

// Created by askar on 7/19/18.
object CryptoConfig {
    const val BASE_URL = BuildConfig.API_CURRENCY

    const val API_VERSION = "/v1"
    const val PUBLIC = "/public"
    const val TICKER_PATH = "/coins"
    const val CURRENCIES_PATH = "$API_VERSION$PUBLIC$TICKER_PATH"
}