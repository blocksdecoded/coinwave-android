package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.coins.chart.IChartsStorage
import org.mockito.Mockito.mock

class ChartsInteractorTest {

    private val coinsStorage = mock(ICoinsRepository::class.java)
    private val chartsStorage = mock(IChartsStorage::class.java)

    private val interactor = ChartsInteractor(coinsStorage, chartsStorage)
}