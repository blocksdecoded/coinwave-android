package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListInteractor
import com.blocksdecoded.coinwave.domain.usecases.list.CurrencyListUseCases
import com.blocksdecoded.coinwave.domain.usecases.postlist.PostInteractor
import com.blocksdecoded.coinwave.domain.usecases.postlist.PostUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.utils.coroutine.AppExecutors
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object UseCaseProvider {
    fun getCurrencyListUseCases(context: Context): CurrencyListUseCases = CurrencyListInteractor(
            AppExecutors.getInstance(),
            ServiceProvider.getCurrencyService(context)
    )

    fun getChartsInteractor(context: Context): ChartsUseCases = ChartsInteractor(
            AppExecutors.getInstance(),
            ServiceProvider.getCurrencyService(context),
            ServiceProvider.getChartsService()
    )

    fun getPostUseCases(): PostUseCases = PostInteractor(
            AppExecutors.getInstance(),
            ServiceProvider.getPostService()
    )

    fun getFavoriteUseCases(context: Context): FavoriteUseCases = FavoriteInteractor(
            SharedStorage.getInstance(context)
    )

    fun getFavoriteChartUseVariant(context: Context): FavoriteChartUseVariant = FavoriteChartInteractor(
            AppExecutors.getInstance(),
            getChartsInteractor(context),
            getFavoriteUseCases(context),
            getCurrencyListUseCases(context)
    )
}