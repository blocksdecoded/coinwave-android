package com.blocksdecoded.coinwave.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipeableViewPager : androidx.viewpager.widget.ViewPager {

    constructor(context: Context) : super(context) {
        setScroller()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setScroller()
    }

    private fun setScroller() {
        try {
            val viewpager = androidx.viewpager.widget.ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, SmoothScroller(context, 0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
