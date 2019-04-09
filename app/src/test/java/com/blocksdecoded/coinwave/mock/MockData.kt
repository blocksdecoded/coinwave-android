package com.blocksdecoded.coinwave.mock

import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import java.util.*

object MockData {
    val postsResponse
            get() = listOf(
                PublisherPost(1, "1234213"),
                PublisherPost(2, "2345677"),
                PublisherPost(3, "3456734")
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