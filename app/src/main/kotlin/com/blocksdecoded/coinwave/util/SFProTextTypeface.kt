package com.blocksdecoded.coinwave.util

import android.content.Context
import android.graphics.Typeface

object SFProTextTypeface {
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/sf_pro_text_regular.otf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/sf_pro_text_semibold.otf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/sf_pro_text_bold.otf")
    }

    fun getLight(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/sf_pro_text_light.otf")
    }
}