package com.blocksdecoded.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.annotation.AnimRes

/**
 * Created by askar on 2/1/19
 * with Android Studio
 */

private const val FAST_ANIM_DURATION = 100L
private const val SHORT_ANIM_DURATION = 200L
private const val MEDIUM_ANIM_DURATION = 400L

fun View.setConstraintTopMargin(value: Int) = try {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.topMargin = value
    layoutParams = params
} catch (e: Exception) {
}

fun View.playScaleAnimation(to: Float) {
    clearAnimation()

    val animation = ScaleAnimation(
        scaleX,
        to,
        scaleY,
        to,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )

    animation.duration = FAST_ANIM_DURATION
    animation.fillAfter = false

    this.startAnimation(animation)
}

fun View.playAnimation(@AnimRes id: Int) {
    val anim = AnimationUtils.loadAnimation(this.context, id)
    this.startAnimation(anim)
}