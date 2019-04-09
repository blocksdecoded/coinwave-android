package com.blocksdecoded.coinwave.domain.usecases.watchlist

import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.mock.FakeData
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class WatchlistInteractorTest {
    private val coinsRepository: ICoinsRepository = mock(ICoinsRepository::class.java)
    private val interactor = WatchlistInteractor(coinsRepository)

    @Test
    fun `Watchlist twice update`() {
        val subject = PublishSubject.create<CoinsResult>()
        `when`(coinsRepository.coinsUpdateSubject).thenReturn(subject)

        val testObserver = interactor.watchlistObservable.test()

        coinsRepository.coinsUpdateSubject.onNext(FakeData.coinsResult)

        testObserver.assertSubscribed()
        testObserver.assertValueAt(0) { it.coins.size == FakeData.savedCoinsCount }

        coinsRepository.coinsUpdateSubject.onNext(FakeData.coinsResult)

        testObserver.assertSubscribed()
        testObserver.assertValueAt(1) { it.coins.size == FakeData.savedCoinsCount }

        testObserver.dispose()
    }
}