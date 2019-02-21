package com.blocksdecoded.rateus.base

import android.graphics.Typeface

/**
 * Created by Tameki on 3/21/18.
 */
interface RateUsDialogContract {
    fun setTitleTypeface(typeface: Typeface): RateUsDialogContract

    fun setDescriptionTypeface(typeface: Typeface): RateUsDialogContract

    fun setPositiveTypeface(typeface: Typeface): RateUsDialogContract

    fun setNegativeTypeface(typeface: Typeface): RateUsDialogContract

    fun setButtonsTypeface(typeface: Typeface): RateUsDialogContract

    fun showRate(): RateUsDialogContract

    fun dismissRate(callListener: Boolean = true)

    fun showFeedbackThanks(): Boolean

    fun setListener(listener: RateUsListener): RateUsDialogContract
}