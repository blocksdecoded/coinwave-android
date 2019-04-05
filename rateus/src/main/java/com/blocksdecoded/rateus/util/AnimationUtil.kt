package com.blocksdecoded.rateus.util

import android.view.View
import android.animation.Animator
import android.animation.AnimatorListenerAdapter

// Created by askar on 5/24/18.

internal object AnimationUtil {
    fun crossFade(from: ArrayList<View?>, to: ArrayList<View?>, maxAlpha: Float) {
        val animationDuration = 100L

        to.forEach {
            it?.alpha = 0f
            it?.visibility = View.VISIBLE
        }

        to.forEach {
            it?.animate()
                    ?.alpha(maxAlpha)
                    ?.setDuration(animationDuration)
                    ?.setListener(null)
        }

        from.forEach {
            it?.animate()
                    ?.alpha(0f)
                    ?.setDuration(animationDuration)
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            it.visibility = View.INVISIBLE
                        }
                    })
        }
    }
}