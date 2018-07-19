package com.makeuseof.cryptocurrency.data.crypto.network

// Created by askar on 7/19/18.
object CryptoConfig {
    const val BASE_URL = "https://api.coinmarketcap.com"
    const val API_VERSION = "/v2"
    const val TICKER_PATH = "/ticker"
    const val CURRENCIES_PATH = "$API_VERSION$TICKER_PATH"
}