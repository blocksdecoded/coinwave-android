package com.blocksdecoded.coinwave.presentation.posts.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Created by askar on 2/5/19
 * with Android Studio
 */

fun View.setRandomBg(seed: Int) {
    background = PostBgUtil.getRandomGradient(seed)
}

object PostBgUtil {
    private const val MIN_COLOR = 50
    private const val MAX_COLOR = 170

    fun getRandomGradient(seed: Int): Drawable {
        val random = Random(seed)

        val r = random.nextInt(MIN_COLOR..MAX_COLOR)
        val g = random.nextInt(MIN_COLOR..MAX_COLOR)
        val b = random.nextInt(MIN_COLOR..MAX_COLOR)

        return getGradientDrawable(
                Color.rgb(r.minusPercent(), g.minusPercent(), b.minusPercent()),
                Color.rgb(r, g, b)
        )
    }

    private fun Int.minusPercent(percent: Float = 0.5f): Int = (this - (this * percent)).toInt()

    private fun getGradientDrawable(start: Int, end: Int): Drawable {
        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(start, end))
        gd.cornerRadius = 0f
        return gd
    }

    fun getBackground(seed: Int): Int {
        val random = Random(seed)

        val r = random.nextInt(MIN_COLOR..MAX_COLOR)
        val g = random.nextInt(MIN_COLOR..MAX_COLOR)
        val b = random.nextInt(MIN_COLOR..MAX_COLOR)

        return Color.rgb(r, g, b)
    }
}