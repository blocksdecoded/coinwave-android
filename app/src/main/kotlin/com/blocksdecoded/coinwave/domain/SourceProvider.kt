package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsService
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsSourceContract
import com.blocksdecoded.coinwave.data.crypto.CurrencyRepository
import com.blocksdecoded.coinwave.data.crypto.CurrencySourceContract
import com.blocksdecoded.coinwave.data.crypto.remote.CurrencyApiClient
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.PostRemoteDataSource
import com.blocksdecoded.coinwave.data.watchlist.WatchlistService
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object SourceProvider {
    fun getPostSource(): PostDataSource = PostRepository.getInstance(
            null,
            PostRemoteDataSource
    )

    fun getChartsSource(): ChartsSourceContract = ChartsService

    fun getCurrencySource(context: Context): CurrencySourceContract =
            CurrencyRepository.getInstance(
                    CurrencyApiClient,
                    getWatchlistSource(context)
            )

    fun getWatchlistSource(context: Context): WatchlistSourceContract =
            WatchlistService.getInstance(SharedStorage.getInstance(context))
}