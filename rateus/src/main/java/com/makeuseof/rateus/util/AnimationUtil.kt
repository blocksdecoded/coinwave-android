package com.makeuseof.rateus.util

import android.view.View
import android.animation.Animator
import android.animation.AnimatorListenerAdapter

// Created by askar on 5/24/18.

object AnimationUtil {
    fun crossFade(from: ArrayList<View?>, to: ArrayList<View?>, maxAlpha: Float) {
        val animationDuration = 100L
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        to.forEach {
            it?.alpha = 0f
            it?.visibility = View.VISIBLE
        }

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        to.forEach {
            it?.animate()
                    ?.alpha(maxAlpha)
                    ?.setDuration(animationDuration)
                    ?.setListener(null)
        }

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
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