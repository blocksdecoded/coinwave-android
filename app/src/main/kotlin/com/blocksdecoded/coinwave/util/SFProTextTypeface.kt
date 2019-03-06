package com.blocksdecoded.coinwave.util

import android.content.Context
import android.graphics.Typeface

// Created by askar on 5/29/18.
object SFProTextTypeface {
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Regular.otf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Semibold.otf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Bold.otf")
    }

    fun getHeavy(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Bold.otf")
    }

    fun getMedium(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Regular.otf")
    }

    fun getThin(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Light.otf")
    }

    fun getLight(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/SF-Pro-Text-Light.otf")
    }
}