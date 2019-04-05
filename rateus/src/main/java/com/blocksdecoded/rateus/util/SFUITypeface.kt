package com.blocksdecoded.rateus.util

import android.content.Context
import android.graphics.Typeface

// Created by askar on 5/24/18.

internal object SFUITypeface {
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Regular.otf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Semibold.otf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Bold.otf")
    }
}