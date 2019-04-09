package com.blocksdecoded.coinwave.di

import com.blocksdecoded.coinwave.data.bootstrap.BootstrapApiClient
import com.blocksdecoded.coinwave.data.bootstrap.IBootstrapClient
import com.blocksdecoded.coinwave.data.coins.CoinsRepository
import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.coins.chart.ChartsStorage
import com.blocksdecoded.coinwave.data.coins.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.coins.local.CoinsLocalStorage
import com.blocksdecoded.coinwave.data.coins.local.ICoinsLocalStorage
import com.blocksdecoded.coinwave.data.coins.remote.CoinApiClient
import com.blocksdecoded.coinwave.data.coins.remote.ICoinClient
import com.blocksdecoded.coinwave.data.coins.remote.ICoinClientConfig
import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.PostRepository
import com.blocksdecoded.coinwave.data.post.remote.IPostClient
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
import com.blocksdecoded.coinwave.domain.usecases.watchlist.IWatchlistUseCases
import com.blocksdecoded.coinwave.domain.usecases.watchlist.WatchlistInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.FavoriteChartInteractor
import com.blocksdecoded.coinwave.domain.variant.favoritechart.IFavoriteChartUseVariant
import com.blocksdecoded.coinwave.presentation.addtowatchlist.IAddToWatchlistContract
import com.blocksdecoded.coinwave.presentation.addtowatchlist.AddToWatchlistPresenter
import com.blocksdecoded.coinwave.presentation.coininfo.ICoinInfoContract
import com.blocksdecoded.coinwave.presentation.coininfo.CoinInfoPresenter
import com.blocksdecoded.coinwave.presentation.coins.ICoinsContract
import com.blocksdecoded.coinwave.presentation.coins.CoinsPresenter
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.coinwave.presentation.pickfavorite.IPickFavoriteContract
import com.blocksdecoded.coinwave.presentation.pickfavorite.PickFavoritePresenter
import com.blocksdecoded.coinwave.presentation.posts.PostsViewModel
import com.blocksdecoded.coinwave.presentation.watchlist.IWatchListContract
import com.blocksdecoded.coinwave.presentation.watchlist.WatchListPresenter
import com.blocksdecoded.utils.shared.ISharedStorage
import com.blocksdecoded.utils.shared.SharedStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dataModule = module {
    single { object : ICoinClientConfig {
        override var ipfsUrl: String = ""
    } as ICoinClientConfig }

    single { CoinApiClient(get()) as ICoinClient }

    single { CoinsLocalStorage(get()) as ICoinsLocalStorage }

    single { SharedStorage(androidApplication()) as ISharedStorage }

    single { CoinsRepository(get(), get(), get()) as ICoinsRepository }

    single { WatchlistStorage(get()) as IWatchlistStorage }

    single { ChartsStorage(get()) as IChartsStorage }

    single { PostApiClient() as IPostClient }

    single { PostRepository(null, get()) as IPostStorage }
}

val domainModule = module {
    factory { CoinsInteractor(get()) as ICoinsUseCases }

    factory { WatchlistInteractor(get()) as IWatchlistUseCases }

    factory { ChartsInteractor(get(), get()) as IChartsUseCases }

    factory { FavoriteInteractor(get()) as IFavoriteUseCases }

    factory { FavoriteChartInteractor(get(), get(), get()) as IFavoriteChartUseVariant }

    factory { PostsInteractor(get()) as IPostsUseCases }
}

val presentationModule = module {
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

    factory { params -> CoinsPresenter(
        params.component1() as ICoinsContract.View,
        params.component2() as IMenuClickListener,
        get()
    ) as ICoinsContract.Presenter }

    factory { params -> AddToWatchlistPresenter(
        params.component1() as IAddToWatchlistContract.View,
        get()
    ) as IAddToWatchlistContract.Presenter }

    viewModel { params -> PostsViewModel(get(), params.component1() as IMenuClickListener) }
}

val bootstrapModule = module {
    single { BootstrapApiClient() as IBootstrapClient }
}

val coinApp = listOf(dataModule, domainModule, presentationModule, bootstrapModule)