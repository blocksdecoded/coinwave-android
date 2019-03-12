package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.crypto.CoinsRepository
import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.local.CoinsLocalStorage
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.watchlist.WatchlistService
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.utils.shared.ISharedStorage
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object SourceProvider {

    private lateinit var mWatchlistService: WatchlistSourceContract
    private lateinit var mCoinsLocalDataSource: ICoinsStorage
    private lateinit var mSharedStorage: ISharedStorage
    private lateinit var mCoinsSource: ICoinsStorage

    fun init(context: Context) {
        mSharedStorage = SharedStorage.getInstance(context.applicationContext)

        mCoinsLocalDataSource = CoinsLocalStorage(mSharedStorage)

        mWatchlistService = WatchlistService(SharedStorage.getInstance(context))

        mCoinsSource = CoinsRepository(
            CoinApiClient,
            mWatchlistService,
            mCoinsLocalDataSource
        )
    }

    fun getSharedStorage() = mSharedStorage

    fun getChartsSource(): IChartsStorage = ChartsStorage

    fun getCoinsSource(): ICoinsStorage = mCoinsSource
}