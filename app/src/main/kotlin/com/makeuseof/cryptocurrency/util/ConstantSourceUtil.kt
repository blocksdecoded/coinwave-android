package com.makeuseof.cryptocurrency.util

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.glide.loadSvg

// Created by askar on 7/23/18.
object ConstantSourceUtil {
    fun getIconUrl(currencyEntity: CurrencyEntity): String{
        return "https://s2.coinmarketcap.com/static/img/coins/32x32/${currencyEntity.id}.png"
    }
}

fun ImageView.loadIcon(currencyEntity: CurrencyEntity){
//    loadSvg(currencyEntity.iconUrl)
    Log.d("ololo", currencyEntity.iconUrl)
    Glide.with(this)
            .load(ConstantSourceUtil.getIconUrl(currencyEntity))
//            .load(currencyEntity.iconUrl)
            .into(this)
}