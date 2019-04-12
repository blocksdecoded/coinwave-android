package com.blocksdecoded.coinwave

import com.blocksdecoded.coinwave.data.coins.remote.model.ChartListResponse
import com.blocksdecoded.coinwave.data.coins.remote.model.HistoryResponse
import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.chart.ChartDataEntry
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.blocksdecoded.coinwave.data.model.coin.CoinsDataResponse
import com.blocksdecoded.coinwave.data.model.coin.CoinsResponse
import com.blocksdecoded.coinwave.data.model.coin.CoinsResult
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


    val coinsResponse
        get() = CoinsResponse("success", coinsDataResponse)

    val coinsDataResponse
        get() = CoinsDataResponse(coinsList, Date())

    const val savedCoinsCount = 2
    val coinsList
            get() = listOf(
                CoinEntity(1, isSaved = true),
                CoinEntity(2, isSaved = false),
                CoinEntity(3, isSaved = true),
                CoinEntity(4, isSaved = false)
            )

    val dailyChart
        get() = HistoryResponse(
            "success",
            ChartListResponse(0f, chartListData)
        )

    val chartData
        get() = ChartData(chartListData)

    val chartListData
        get() = listOf(
            ChartDataEntry("12.3", Date().time),
            ChartDataEntry("13.3", Date().time + 2000),
            ChartDataEntry("14.3", Date().time + 2000),
            ChartDataEntry("15.3", Date().time + 2000)
        )
}