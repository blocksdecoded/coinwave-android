package com.blocksdecoded.utils.glide

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

/**
 * Created by askar on 2/1/19
 * with Android Studio
 */

fun ImageView.loadSvg(url: String){
    val requestBuilder = GlideApp.with(this)
            .`as`(PictureDrawable::class.java)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())

    requestBuilder
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .load(url)
            .into(this)
}