package com.makeuseof.utils

import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView

// Created by askar on 5/24/18.

fun androidx.recyclerview.widget.RecyclerView.ViewHolder.updateHeight(height: Int, animated: Boolean = false){
    if (animated){
        val start = this.itemView.layoutParams.height

        if (start == height){
            setHeight(height)
            return
        }

        val valueAnimator = ValueAnimator.ofInt(start, height)

        valueAnimator.addUpdateListener {
            setHeight(it.animatedValue as Int)
            this.itemView.invalidate()
        }

        valueAnimator.duration = 200
        valueAnimator.start()
    } else {
        setHeight(height)
    }
}

fun androidx.recyclerview.widget.RecyclerView.ViewHolder.setHeight(height: Int){
    this.itemView.layoutParams.height = height
}