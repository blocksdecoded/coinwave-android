package com.blocksdecoded.coinwave.presentation.widgets

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

class SmoothScroller(
    context: Context?,
    private var scrollDuration: Int = 250
) : Scroller(context, DecelerateInterpolator()) {

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }
}