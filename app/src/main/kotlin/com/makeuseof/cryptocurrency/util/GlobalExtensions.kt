package com.makeuseof.cryptocurrency.util

import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.isValidIndex

// Created by askar on 7/23/18.
fun List<CurrencyEntity>.findCurrency(currencyEntity: CurrencyEntity, body: ((index: Int) -> Unit)? = null): Int{
    val index = this.indexOfFirst { it.id == currencyEntity.id }
    if(this.isValidIndex(index)){
        body?.invoke(index)
    }
    return index
}

fun ArrayList<CurrencyEntity>.addSortedByRank(currencyEntity: CurrencyEntity){
    this.add(currencyEntity)
    this.sortBy { it.rank }
}