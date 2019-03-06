package com.blocksdecoded.coinwave.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.util.*

/**
 * Created by Tameki on 2/19/18.
 */
class FontTextView : TextView {
    constructor(context: Context) : super(context) {
        setTypeface(2)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.FontTextView,
                0, 0)
        try {
            val mFont = a.getInt(R.styleable.FontTextView_ftv_typeface, 2)
            setTypeface(mFont)
        } finally {
            a.recycle()
        }

        includeFontPadding = false
    }

    private fun setTypeface(index: Int) {
        when (index) {
            0 -> this.setThinFont()
            1 -> this.setLightFont()
            2 -> this.setRegularFont()
            3 -> this.setMediumFont()
            4 -> this.setSemiboldFont()
            5 -> this.setBoldFont()
            6 -> this.setHeavyFont()
            7 -> this.setHeavyFont()
        }
    }
}