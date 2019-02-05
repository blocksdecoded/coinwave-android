package com.makeuseof.cryptocurrency.view.postlist.util

import android.graphics.Color
import android.widget.ImageView
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Created by askar on 2/5/19
 * with Android Studio
 */

fun ImageView.setRandomBg(seed: Int) {
    setBackgroundColor(PostBgUtil.getBackground(seed))
}

object PostBgUtil {
    private const val MIN_COLOR = 0
    private const val MAX_COLOR = 100

    fun getBackground(seed: Int): Int {
        val random = Random(seed)
        val r = random.nextInt(MIN_COLOR..MAX_COLOR)
        val g = random.nextInt(MIN_COLOR..MAX_COLOR)
        val b = random.nextInt(MIN_COLOR..MAX_COLOR)
        return Color.rgb(r, g, b)
    }
}