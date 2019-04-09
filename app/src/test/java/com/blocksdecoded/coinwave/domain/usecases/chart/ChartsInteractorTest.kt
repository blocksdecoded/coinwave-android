package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.coins.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.model.ChartData
import com.blocksdecoded.coinwave.data.model.ChartDataEntry
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.*

class ChartsInteractorTest {
    private val coinsStorage = mock(ICoinsRepository::class.java)
    private val chartsStorage = mock(IChartsStorage::class.java)

    private val interactor = ChartsInteractor(coinsStorage, chartsStorage)

    @Test
    fun `Request chart data for same period only once`() {
        val id = 1
        val symbol = "symbol"
        `when`(coinsStorage.getCoin(id)).thenReturn(CoinEntity(id, "name", symbol))
        `when`(chartsStorage.getChart(symbol)).thenReturn(
            Single.just(ChartData(listOf(
                ChartDataEntry("", 1111111),
                ChartDataEntry("", 1111112),
                ChartDataEntry("", 1111113)
            ))))

        interactor.getChartData(id).test().dispose()
        interactor.getChartData(id).test().dispose()
        interactor.getChartData(id).test().dispose()

        verify(coinsStorage, times(1)).getCoin(id)
        verify(chartsStorage, times(1)).getChart(symbol)
    }
}