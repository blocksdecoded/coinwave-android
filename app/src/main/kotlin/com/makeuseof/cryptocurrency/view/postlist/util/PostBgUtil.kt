package com.makeuseof.cryptocurrency.view.postlist.util

import android.graphics.Color
import android.widget.ImageView
import kotlin.random.Random

/**
 * Created by askar on 2/5/19
 * with Android Studio
 */

fun ImageView.setRandomBg() {
    setBackgroundColor(PostBgUtil.getBackground())
}

object PostBgUtil {
    fun getBackground(): Int = Color.rgb(
            getRandomChannel(),
            getRandomChannel(),
            getRandomChannel()
    )

    private fun getRandomChannel(): Int = Random.nextInt(0, 90)
}