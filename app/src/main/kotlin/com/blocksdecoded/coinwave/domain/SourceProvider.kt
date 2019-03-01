package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsService
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsSourceContract
import com.blocksdecoded.coinwave.data.crypto.CoinsRepository
import com.blocksdecoded.coinwave.data.crypto.CoinsDataSource
import com.blocksdecoded.coinwave.data.crypto.local.CoinsLocalDataSource
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.PostRemoteDataSource
import com.blocksdecoded.coinwave.data.watchlist.WatchlistService
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.shared.SharedContract
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object SourceProvider {

    private lateinit var mWatchlistService: WatchlistSourceContract
    private lateinit var mCoinsLocalDataSource: CoinsDataSource
    private lateinit var mSharedStorage: SharedContract
    private lateinit var mPostSource: PostDataSource
    private lateinit var mCoinsSource: CoinsDataSource

    fun init(context: Context) {
        mSharedStorage = SharedStorage.getInstance(context.applicationContext)

        mCoinsLocalDataSource = CoinsLocalDataSource(mSharedStorage)

        mWatchlistService = WatchlistService.getInstance(SharedStorage.getInstance(context))

        mPostSource = PostRepository.getInstance(null, PostRemoteDataSource)

        mCoinsSource = CoinsRepository.getInstance(
            CoinApiClient,
            mWatchlistService,
            mCoinsLocalDataSource
        )
    }

    fun getPostSource(): PostDataSource = mPostSource

    fun getChartsSource(): ChartsSourceContract = ChartsService

    fun getCoinsSource(): CoinsDataSource = mCoinsSource
}