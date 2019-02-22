package com.blocksdecoded.coinwave.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * Created by askar on 11/22/18
 * with Android Studio
 */
class LockableScrollView : ScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    var mScrollable = true

    override fun onTouchEvent(ev: MotionEvent): Boolean = when (ev.action) {
        MotionEvent.ACTION_DOWN ->
            // if we can scroll pass the event to the superclass
            mScrollable && super.onTouchEvent(ev)
        else -> super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return mScrollable && super.onInterceptTouchEvent(ev)
    }
}