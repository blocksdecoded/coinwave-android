package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.crypto.ICoinsStorage
import com.blocksdecoded.coinwave.data.crypto.chart.IChartsStorage
import org.junit.Assert.*
import org.mockito.Mockito.mock

class ChartsInteractorTest {

    private val coinsStorage = mock(ICoinsStorage::class.java)
    private val chartsStorage = mock(IChartsStorage::class.java)

    private val interactor = ChartsInteractor(coinsStorage, chartsStorage)
}