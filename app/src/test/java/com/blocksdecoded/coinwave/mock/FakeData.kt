package com.blocksdecoded.coinwave.mock

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import java.util.*

object FakeData {
    val postsResponse
            get() = listOf(
                PublisherPost(1, "1234213", "", "", "", "", null)
            )

    val coinsResult
        get() = CoinsResult(coinsList, Date())

    const val savedCoinsCount = 2
    val coinsList
            get() = listOf(
                CoinEntity(1, isSaved = true),
                CoinEntity(2, isSaved = false),
                CoinEntity(3, isSaved = true),
                CoinEntity(4, isSaved = false)
            )
}