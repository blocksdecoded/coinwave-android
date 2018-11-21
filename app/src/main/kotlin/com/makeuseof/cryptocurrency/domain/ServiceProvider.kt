package com.makeuseof.cryptocurrency.domain

import android.content.Context
import com.makeuseof.cryptocurrency.data.chart.ChartsService
import com.makeuseof.cryptocurrency.data.chart.ChartsSourceContract
import com.makeuseof.cryptocurrency.data.crypto.CurrencyService
import com.makeuseof.cryptocurrency.data.crypto.CurrencySourceContract
import com.makeuseof.cryptocurrency.data.post.PostDataSource
import com.makeuseof.cryptocurrency.data.post.PostRepository
import com.makeuseof.cryptocurrency.data.post.remote.PostRemoteDataSource
import com.makeuseof.cryptocurrency.data.watchlist.WatchlistService
import com.makeuseof.cryptocurrency.data.watchlist.WatchlistSourceContract
import com.makeuseof.utils.shared.SharedStorage

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