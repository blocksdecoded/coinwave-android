package com.blocksdecoded.coinwave.di

import com.blocksdecoded.coinwave.data.crypto.CoinsRepository
import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClient
import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.PostApiClient
import com.blocksdecoded.coinwave.data.watchlist.WatchlistService
import com.blocksdecoded.coinwave.data.watchlist.WatchlistSourceContract
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsInteractor
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsInteractor
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsUseCases
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsInteractor
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartUseVariant
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoPresenter
import com.blocksdecoded.coinwave.presentation.coinslist.CoinsListContract
import com.blocksdecoded.coinwave.presentation.coinslist.CoinsListPresenter
import com.blocksdecoded.coinwave.presentation.main.MenuClickListener
import com.blocksdecoded.coinwave.presentation.posts.PostsContract
import com.blocksdecoded.coinwave.presentation.posts.PostsPresenter
import com.blocksdecoded.utils.shared.ISharedStorage
import com.blocksdecoded.utils.shared.SharedStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val useCaseModule = module {
    factory { CoinsInteractor(get()) as CoinsUseCases }

    factory { ChartsInteractor(get(), get()) as ChartsUseCases }

    factory { FavoriteInteractor(get()) as FavoriteUseCases }

    factory { FavoriteChartInteractor(get(), get(), get()) as FavoriteChartUseVariant }
}

val sourceModule = module {
    single { SharedStorage(androidApplication()) as ISharedStorage }

    single { CoinsRepository(get(), get(), null) as ICoinsStorage }

    single { WatchlistService(get()) as WatchlistSourceContract }

    single { ChartsStorage as IChartsStorage }

    single { CoinApiClient as ICoinClient }
}

val coinsModule = module {

    factory { params -> }

    factory { params -> CoinsListPresenter(
        params.component1() as CoinsListContract.View,
        params.component2() as MenuClickListener,
        get()
    ) as CoinsListContract.Presenter }

}

val postModule = module {

    single { PostRepository(null, PostApiClient) as IPostStorage }

    factory { PostsInteractor(get()) as PostsUseCases }

    factory { params -> PostsPresenter(
        params.component1() as PostsContract.View,
        params.component2() as MenuClickListener,
        get()
    ) as PostsContract.Presenter }

}

val coinApp = listOf(useCaseModule, sourceModule, postModule, coinsModule)