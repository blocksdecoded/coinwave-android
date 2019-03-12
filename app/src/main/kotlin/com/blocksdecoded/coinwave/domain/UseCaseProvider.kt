package com.blocksdecoded.coinwave.domain

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

// Created by askar on 7/19/18.
object UseCaseProvider {
    val coinsUseCase: CoinsUseCases
        get() = CoinsInteractor(SourceProvider.getCoinsSource())

    val chartsUseCase: ChartsUseCases
        get() = ChartsInteractor(
                SourceProvider.getCoinsSource(),
                SourceProvider.getChartsSource()
        )

    val favoriteUseCase: FavoriteUseCases
        get() = FavoriteInteractor(SourceProvider.getSharedStorage())

    val favoriteChartUseCase: FavoriteChartUseVariant
        get() = FavoriteChartInteractor(chartsUseCase, favoriteUseCase, coinsUseCase)
}