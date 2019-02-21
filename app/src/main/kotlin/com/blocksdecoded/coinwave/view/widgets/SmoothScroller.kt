package com.blocksdecoded.coinwave.view.widgets

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

class SmoothScroller : Scroller {

    constructor(context: Context?, scrollTime: Int = 250): super(context, DecelerateInterpolator()) {
        SCROLL_TIME = scrollTime
    }

    private var SCROLL_TIME = 250

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, SCROLL_TIME)
    }
}