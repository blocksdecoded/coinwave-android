package com.makeuseof.cryptocurrency.util

import android.content.Context
import android.graphics.Typeface

// Created by askar on 7/26/18.
object OpenSansTypeface {
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/OpenSans-Regular.ttf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/OpenSans-SemiBold.ttf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/OpenSans-Bold.ttf")
    }

    fun getLight(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/OpenSans-Light.ttf")
    }
}