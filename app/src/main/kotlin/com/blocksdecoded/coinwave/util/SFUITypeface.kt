package com.blocksdecoded.coinwave.util

import android.content.Context
import android.graphics.Typeface

// Created by askar on 5/29/18.
object SFUITypeface {
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Regular.otf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Semibold.otf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Bold.otf")
    }

    fun getHeavy(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Heavy.otf")
    }

    fun getMedium(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Medium.otf")
    }

    fun getThin(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Thin.otf")
    }

    fun getLight(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SFUIDisplay-Light.otf")
    }
}