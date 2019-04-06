package com.blocksdecoded.coinwave.presentation.widgets

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

    var scrollable = true

    override fun onTouchEvent(ev: MotionEvent): Boolean = when (ev.action) {
        MotionEvent.ACTION_DOWN ->
            scrollable && super.onTouchEvent(ev)
        else -> super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
    }
}