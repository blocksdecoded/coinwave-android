package com.blocksdecoded.rateus.widget

import android.graphics.Typeface

/**
 * Created by Tameki on 3/21/18.
 */
interface IRateUsDialog {
    fun setTitleTypeface(typeface: Typeface): IRateUsDialog

    fun setDescriptionTypeface(typeface: Typeface): IRateUsDialog

    fun setPositiveTypeface(typeface: Typeface): IRateUsDialog

    fun setNegativeTypeface(typeface: Typeface): IRateUsDialog

    fun setButtonsTypeface(typeface: Typeface): IRateUsDialog

    fun showRate(): IRateUsDialog

    fun dismissRate(callListener: Boolean = true)

    fun showFeedbackThanks(): Boolean

    fun setListener(listener: IRateUsListener): IRateUsDialog
}