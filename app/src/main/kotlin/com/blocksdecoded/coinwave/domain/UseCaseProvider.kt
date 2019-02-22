package com.blocksdecoded.coinwave.domain

import android.content.Context
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsInteractor
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsInteractor
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.utils.shared.SharedStorage

// Created by askar on 7/19/18.
object UseCaseProvider {
    fun getCoinsUseCases(context: Context): CoinsUseCases = CoinsInteractor(
            SourceProvider.getCoinsSource(context)
    )

    fun getChartsInteractor(context: Context): ChartsUseCases = ChartsInteractor(
            SourceProvider.getCoinsSource(context),
            SourceProvider.getChartsSource()
    )

    fun getPostUseCases(): PostsUseCases = PostsInteractor(
            SourceProvider.getPostSource()
    )

    fun getFavoriteUseCases(context: Context): FavoriteUseCases = FavoriteInteractor(
            SharedStorage.getInstance(context)
    )

    fun getFavoriteChartUseVariant(context: Context): FavoriteChartUseVariant = FavoriteChartInteractor(
            getChartsInteractor(context),
            getFavoriteUseCases(context),
            getCoinsUseCases(context)
    )
}