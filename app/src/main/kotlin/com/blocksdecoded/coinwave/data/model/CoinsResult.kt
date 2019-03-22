package com.blocksdecoded.coinwave.data.model

import java.util.*

data class CoinsResult(
        val coins: List<CoinEntity>,
        val lastUpdated: Date
) {
}