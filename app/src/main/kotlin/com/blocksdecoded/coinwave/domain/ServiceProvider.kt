package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.data.chart.ChartsService
import com.blocksdecoded.coinwave.data.chart.ChartsSourceContract
import com.blocksdecoded.coinwave.data.crypto.CurrencyService
import com.blocksdecoded.coinwave.data.crypto.CurrencySourceContract
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.PostRemoteDataSource
import com.blocksdecoded.coinwave.data.watchlist.WatchlistService
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object ServiceProvider {

    fun getPostService(): PostDataSource = PostRepository.getInstance(
            null,
            PostRemoteDataSource.getInstance()
    )

    fun getChartsService(): ChartsSourceContract = ChartsService.getInstance()

    fun getCurrencyService(context: Context): CurrencySourceContract =
            CurrencyService.getInstance(getWatchlistService(context))

    fun getWatchlistService(context: Context): WatchlistSourceContract =
            WatchlistService.getInstance(SharedStorage.getInstance(context))
}