package com.makeuseof.cryptocurrency.util

import android.widget.TextView
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.ResourceUtil
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

fun TextView.setChangedPercent(percent: Float){
    text = "${if (percent > 0) "+" else ""}$percent%"
    this.setTextColor(
            if(percent >= 0f){
                ResourceUtil.getColor(this.context, R.color.green)
            } else {
                ResourceUtil.getColor(this.context, R.color.red)
            }
    )
}