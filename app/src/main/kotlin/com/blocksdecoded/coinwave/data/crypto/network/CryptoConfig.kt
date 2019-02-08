package com.blocksdecoded.coinwave.data.crypto.network

import com.blocksdecoded.coinwave.BuildConfig

// Created by askar on 7/19/18.
object CryptoConfig {
    const val BASE_URL = BuildConfig.API_CURRENCY

    const val API_VERSION = "/v1"
    const val PUBLIC = "/public"
    const val COINS_PATH = "/coins"
    const val CURRENCIES_PATH = "$API_VERSION$PUBLIC$COINS_PATH"
}