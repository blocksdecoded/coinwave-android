package com.makeuseof.cryptocurrency.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.squareup.picasso.Picasso

// Created by askar on 7/23/18.
object ConstantSourceUtil {
    fun getIconUrl(currencyEntity: CurrencyEntity): String{
        return "https://s2.coinmarketcap.com/static/img/coins/32x32/${currencyEntity.id}.png"
    }
}

fun ImageView.loadIcon(currencyEntity: CurrencyEntity){

    Glide.with(this)
            .load(ConstantSourceUtil.getIconUrl(currencyEntity))
            .into(this)
//    Picasso.get()
//            .load(ConstantSourceUtil.getIconUrl(currencyEntity))
//            .into(this)
}