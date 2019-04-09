package com.blocksdecoded.rateus.widget

import androidx.annotation.FontRes

/**
 * Created by Tameki on 3/21/18.
 */
interface IRateUsDialog {
    fun setTitleFont(@FontRes font: Int): IRateUsDialog

    fun setDescriptionFont(@FontRes font: Int): IRateUsDialog

    fun setPositiveFont(@FontRes font: Int): IRateUsDialog

    fun setNegativeFont(@FontRes font: Int): IRateUsDialog

    fun setButtonsFont(@FontRes font: Int): IRateUsDialog

    fun showRate(): IRateUsDialog

    fun dismissRate(callListener: Boolean = true)

    fun showFeedbackThanks(): Boolean

    fun setListener(listener: IRateUsListener): IRateUsDialog
}