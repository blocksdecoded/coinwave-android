package com.blocksdecoded.coinwave.domain.usecases.coins

import com.blocksdecoded.coinwave.data.model.CoinsResult
import com.blocksdecoded.coinwave.domain.usecases.BaseCoinsUseCase
import io.reactivex.subjects.PublishSubject

// Created by askar on 7/19/18.
interface ICoinsUseCases : BaseCoinsUseCase {
    val coinsUpdateSubject: PublishSubject<CoinsResult>

    fun saveCoin(id: Int): Boolean

    fun removeCoin(id: Int): Boolean
}