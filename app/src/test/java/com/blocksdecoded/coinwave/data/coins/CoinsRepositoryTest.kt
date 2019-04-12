package com.blocksdecoded.coinwave.data.coins

import com.blocksdecoded.coinwave.MockData
import com.blocksdecoded.coinwave.data.coins.local.ICoinsLocalStorage
import com.blocksdecoded.coinwave.data.coins.remote.ICoinClient
import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.watchlist.IWatchlistStorage
import com.nhaarman.mockito_kotlin.atLeast
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CoinsRepositoryTest {
    private val coinsClient: ICoinClient = mock(ICoinClient::class.java)
    private val coinsLocalStorage: ICoinsLocalStorage = mock(ICoinsLocalStorage::class.java)
    private val watchlistStorage: IWatchlistStorage = mock(IWatchlistStorage::class.java)

    private lateinit var repository: CoinsRepository

    @Before
    fun setup() {
        `when`(coinsLocalStorage.getAllCoins()).thenReturn(Observable.just(MockData.coinsDataResponse))
        `when`(coinsClient.getCoins()).thenReturn(Single.just(MockData.coinsResponse))

        repository = CoinsRepository(coinsClient, coinsLocalStorage, watchlistStorage)
    }

    @Test
    fun `Fetch coins directly from client`() {
        val testObserver = repository.coinsUpdateSubject.test()

        repository.getAllCoins(skipCache = true, force = true)
            .test()
            .dispose()

        testObserver.assertSubscribed()
            .assertValueCount(1)
            .dispose()
    }

    @Test
    fun `Fetch coins only from cache`() {
        repository.getAllCoins(skipCache = true, force = true).test()
            .assertSubscribed()
            .assertValueCount(2)
            .dispose()

        repository.getAllCoins(skipCache = false, force = false).test()
            .assertSubscribed()
            .assertValueCount(1)
            .dispose()
    }

    @Test
    fun `Client call only if cache is dirty`() {
        repository.getAllCoins(skipCache = true, force = true).test()
            .assertSubscribed()
            .assertValueCount(2)
            .dispose()

        repository.getAllCoins(skipCache = true, force = false).test()
            .assertSubscribed()
            .assertValueCount(1)
            .dispose()
    }

    @Test
    fun `Call save valid coin`() {
        val id = 1
        repository.saveCoin(id)

        verify(watchlistStorage, atLeastOnce()).addId(id)
    }

    @Test
    fun `Call save invalid coin`() {
        val id = 10
        repository.saveCoin(id)

        verify(watchlistStorage, never()).addId(id)
    }

    @Test
    fun `Call delete valid coin`() {
        val id = 1
        repository.removeCoin(id)

        verify(watchlistStorage, atLeastOnce()).removeId(id)
    }

    @Test
    fun `Call delete invalid coin`() {
        val id = 10
        repository.removeCoin(id)

        verify(watchlistStorage, never()).removeId(id)
    }

    @Test
    fun `Default chart period call`() {
        val coin = "BTC"

        `when`(coinsClient.getHistory(coin, ChartPeriodEnum.DAY)).thenReturn(Single.just(MockData.dailyChart))

        repository.getChart(coin).test().dispose()

        verify(coinsClient, only()).getHistory(coin, ChartPeriodEnum.DAY)
    }
}