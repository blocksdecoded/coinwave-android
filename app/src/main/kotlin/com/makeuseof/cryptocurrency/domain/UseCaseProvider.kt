package com.makeuseof.cryptocurrency.domain

import android.content.Context
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsUseCases
import com.makeuseof.cryptocurrency.domain.usecases.chart.ChartsInteractor
import com.makeuseof.cryptocurrency.domain.usecases.favorite.FavoriteInteractor
import com.makeuseof.cryptocurrency.domain.usecases.favorite.FavoriteUseCases
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListInteractor
import com.makeuseof.cryptocurrency.domain.usecases.list.CurrencyListUseCases
import com.makeuseof.cryptocurrency.domain.usecases.postlist.PostInteractor
import com.makeuseof.cryptocurrency.domain.usecases.postlist.PostUseCases
import com.makeuseof.cryptocurrency.domain.variant.favoritechart.FavoriteChartInteractor
import com.makeuseof.cryptocurrency.domain.variant.favoritechart.FavoriteChartUseVariant
import com.makeuseof.utils.coroutine.AppExecutors
import com.makeuseof.utils.shared.SharedStorage

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