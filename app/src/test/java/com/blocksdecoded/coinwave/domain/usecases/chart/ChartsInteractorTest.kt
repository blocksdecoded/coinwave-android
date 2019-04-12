package com.blocksdecoded.coinwave.domain.usecases.chart

import com.blocksdecoded.coinwave.MockData
import com.blocksdecoded.coinwave.data.coins.ICoinsRepository
import com.blocksdecoded.coinwave.data.coins.chart.IChartsStorage
import com.blocksdecoded.coinwave.data.model.chart.ChartData
import com.blocksdecoded.coinwave.data.model.chart.ChartDataEntry
import com.blocksdecoded.coinwave.data.model.chart.ChartPeriodEnum
import com.blocksdecoded.coinwave.data.model.coin.CoinEntity
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class ChartsInteractorTest {
    private val coinsStorage = mock(ICoinsRepository::class.java)
    private val chartsStorage = mock(IChartsStorage::class.java)

    private val interactor = ChartsInteractor(coinsStorage, chartsStorage)

    @Test
    fun `Request chart data for same period only once`() {
        val id = 1
        val symbol = "symbol"
        `when`(coinsStorage.getCoin(id)).thenReturn(CoinEntity(id, symbol = symbol))
        `when`(chartsStorage.getChart(symbol)).thenReturn(Single.just(MockData.chartData))

        interactor.getChartData(id).test().dispose()
        interactor.getChartData(id).test().dispose()
        interactor.getChartData(id).test().dispose()

        verify(coinsStorage, only()).getCoin(id)
        verify(chartsStorage, only()).getChart(symbol)
    }
}