package com.blocksdecoded.coinwave.data.model.coin

import java.util.*

data class CoinsResult(
    var coins: List<CoinEntity>,
    val lastUpdated: Date
)