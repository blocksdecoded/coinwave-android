package com.blocksdecoded.coinwave.util

import android.widget.ImageView
import com.blocksdecoded.coinwave.data.model.CoinEntity
import com.blocksdecoded.utils.glide.SvgSoftwareLayerSetter
import android.graphics.drawable.PictureDrawable
import com.blocksdecoded.utils.glide.GlideApp
import com.blocksdecoded.utils.logE
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

// Created by askar on 7/23/18.
object ConstantSourceUtil {
    fun getIconUrl(coinEntity: CoinEntity): String {
        return "https://s2.coinmarketcap.com/static/img/coins/32x32/${coinEntity.id}.png"
    }
}

fun ImageView.loadIcon(coinEntity: CoinEntity) = try {
//    loadSvg(coinEntity.iconUrl)

    GlideApp.with(this)
            .`as`(PictureDrawable::class.java)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())
            .load(coinEntity.iconUrl)
            .into(this)

//    Glide.with(this)
//            .load(ConstantSourceUtil.getIconUrl(coinEntity))
//            .load(coinEntity.iconUrl)
//            .into(this)
} catch (e: Exception) {
    logE(e)
}