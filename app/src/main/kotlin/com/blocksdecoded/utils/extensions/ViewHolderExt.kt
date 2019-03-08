package com.blocksdecoded.utils.extensions

import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView

// Created by askar on 5/24/18.

var RecyclerView.ViewHolder.height
    get() = itemView.layoutParams.height
    set(value) {
        itemView.layoutParams.height = value
    }

fun RecyclerView.ViewHolder.updateHeight(height: Int, animated: Boolean = false) {
    if (animated) {
        val start = this.itemView.layoutParams.height

        if (start == height) {
            this.height = height
            return
        }

        val valueAnimator = ValueAnimator.ofInt(start, height)

        valueAnimator.addUpdateListener {
            this.height = it.animatedValue as Int
            this.itemView.invalidate()
        }

        valueAnimator.duration = 200
        valueAnimator.start()
    } else {
        this.height = height
    }
}