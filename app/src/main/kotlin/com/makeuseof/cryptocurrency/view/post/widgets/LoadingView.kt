package com.makeuseof.cryptocurrency.view.post.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar

class LoadingView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        val progress = ProgressBar(context)
        progress.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(progress)
    }
}
