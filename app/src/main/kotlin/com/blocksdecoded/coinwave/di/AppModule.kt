package com.blocksdecoded.coinwave.di

import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.data.bootstrap.BootstrapApiClient
import com.blocksdecoded.coinwave.data.bootstrap.IBootstrapClient
import com.blocksdecoded.coinwave.data.config.ConfigProvider
import com.blocksdecoded.coinwave.data.crypto.CoinsRepository
import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.ChartsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.crypto.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClient
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClientConfig
import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.IPostClient
import com.blocksdecoded.coinwave.data.post.remote.IPostClientConfig
import com.blocksdecoded.coinwave.data.post.remote.PostApiClient
import com.blocksdecoded.coinwave.data.watchlist.WatchlistStorage
import com.blocksdecoded.coinwave.data.watchlist.IWatchlistStorage
import com.blocksdecoded.coinwave.domain.usecases.chart.ChartsInteractor
import com.blocksdecoded.coinwave.domain.usecases.chart.IChartsUseCases
import com.blocksdecoded.coinwave.domain.usecases.coins.CoinsInteractor
import com.blocksdecoded.coinwave.domain.usecases.coins.ICoinsUseCases
import com.blocksdecoded.coinwave.domain.usecases.favorite.FavoriteInteractor
import com.blocksdecoded.coinwave.domain.usecases.favorite.IFavoriteUseCases
import com.blocksdecoded.coinwave.domain.usecases.posts.PostsInteractor
import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.IFavoriteChartUseVariant
import com.blocksdecoded.coinwave.presentation.addtowatchlist.IAddToWatchlistContract
import com.blocksdecoded.coinwave.presentation.addtowatchlist.AddToWatchlistPresenter
import com.blocksdecoded.coinwave.presentation.coininfo.ICoinInfoContract
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoPresenter
import com.blocksdecoded.coinwave.presentation.coinslist.ICoinsListContract
import com.blocksdecoded.coinwave.presentation.coinslist.CoinsListPresenter
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.coinwave.presentation.pickfavorite.IPickFavoriteContract
import com.blocksdecoded.coinwave.presentation.pickfavorite.PickFavoritePresenter
import com.blocksdecoded.coinwave.presentation.posts.IPostsContract
import com.blocksdecoded.coinwave.presentation.posts.PostsPresenter
import com.blocksdecoded.coinwave.presentation.watchlist.IWatchListContract
import com.blocksdecoded.coinwave.presentation.watchlist.WatchListPresenter
import com.blocksdecoded.utils.shared.ISharedStorage
import com.blocksdecoded.utils.shared.SharedStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val useCaseModule = module {
    factory { CoinsInteractor(get()) as ICoinsUseCases }

    factory { ChartsInteractor(get(), get()) as IChartsUseCases }

    factory { FavoriteInteractor(get()) as IFavoriteUseCases }

    factory { FavoriteChartInteractor(get(), get(), get()) as IFavoriteChartUseVariant }
}

val sourceModule = module {
    single { ConfigProvider(
        BuildConfig.API_COINS,
        BuildConfig.API_POSTS
    ) as ICoinClientConfig } bind (IPostClientConfig::class)

    single { CoinApiClient(get()) as ICoinClient }

    single { SharedStorage(androidApplication()) as ISharedStorage }

    single { CoinsRepository(get(), get(), null) as ICoinsStorage }

    single { WatchlistStorage(get()) as IWatchlistStorage }

    single { ChartsStorage(get()) as IChartsStorage }
}

val coinsModule = module {
    factory { params -> PickFavoritePresenter(
        params.component1() as IPickFavoriteContract.View,
        get(),
        get()
    ) as IPickFavoriteContract.Presenter }

    factory { params -> WatchListPresenter(
        params.component1() as IWatchListContract.View,
        params.component2() as IMenuClickListener,
        get(),
        get()
    ) as IWatchListContract.Presenter }

    factory { params -> CoinInfoPresenter(
        params.component1() as ICoinInfoContract.View,
        get(),
        get()
    ) as ICoinInfoContract.Presenter }

    factory { params -> CoinsListPresenter(
        params.component1() as ICoinsListContract.View,
        params.component2() as IMenuClickListener,
        get()
    ) as ICoinsListContract.Presenter }

    factory { params -> AddToWatchlistPresenter(
        params.component1() as IAddToWatchlistContract.View,
        get()
    ) as IAddToWatchlistContract.Presenter }
}

val postModule = module {
    single { PostApiClient() as IPostClient }

    single { PostRepository(null, get()) as IPostStorage }

    factory { PostsInteractor(get()) as IPostsUseCases }

    factory { params -> PostsPresenter(
        params.component1() as IPostsContract.View,
        params.component2() as IMenuClickListener,
        get()
    ) as IPostsContract.Presenter }
}

val bootstrapModule = module {
    single { BootstrapApiClient() as IBootstrapClient }
}

val coinApp = listOf(useCaseModule, sourceModule, postModule, coinsModule, bootstrapModule)