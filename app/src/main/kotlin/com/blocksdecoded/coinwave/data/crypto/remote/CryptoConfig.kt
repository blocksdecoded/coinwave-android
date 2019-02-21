package com.blocksdecoded.coinwave.data.crypto.remote

import com.blocksdecoded.coinwave.BuildConfig

// Created by askar on 7/19/18.
object CryptoConfig {
    const val BASE_URL = BuildConfig.API_CURRENCY

    const val CURRENCIES_PATH = "/index.json"
    const val HISTORY_PATH = "/coin"
}