package com.blocksdecoded.coinwave.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager : ViewPager {

    constructor(context: Context) : super(context) {
        setScroller()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setScroller()
    }

    private fun setScroller() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, SmoothScroller(context, 0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, false)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
