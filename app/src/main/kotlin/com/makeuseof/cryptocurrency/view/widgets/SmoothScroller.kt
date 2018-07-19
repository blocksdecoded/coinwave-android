package com.makeuseof.cryptocurrency.view.widgets

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

class SmoothScroller: Scroller {

    constructor(context: Context?): super(context, DecelerateInterpolator())

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, 350 /*1 sec*/)
    }
}