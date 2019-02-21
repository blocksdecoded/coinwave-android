package com.blocksdecoded.coinwave.util

import android.widget.ImageView
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.utils.glide.SvgSoftwareLayerSetter
import android.graphics.drawable.PictureDrawable
import com.blocksdecoded.utils.glide.GlideApp
import com.blocksdecoded.utils.logE
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

// Created by askar on 7/23/18.
object ConstantSourceUtil {
    fun getIconUrl(currencyEntity: CurrencyEntity): String {
        return "https://s2.coinmarketcap.com/static/img/coins/32x32/${currencyEntity.id}.png"
    }
}

fun ImageView.loadIcon(currencyEntity: CurrencyEntity) = try {
//    loadSvg(currencyEntity.iconUrl)

    GlideApp.with(this)
            .`as`(PictureDrawable::class.java)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())
            .load(currencyEntity.iconUrl)
            .into(this)

//    Glide.with(this)
//            .load(ConstantSourceUtil.getIconUrl(currencyEntity))
//            .load(currencyEntity.iconUrl)
//            .into(this)
} catch (e: Exception) {
    logE(e)
}