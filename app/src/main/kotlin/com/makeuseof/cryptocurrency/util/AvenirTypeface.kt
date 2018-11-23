package com.makeuseof.cryptocurrency.util

import android.content.Context
import android.graphics.Typeface

// Created by askar on 9/13/18.
object AvenirTypeface{
    fun getRegular(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Book.ttf")
    }

    fun getSemibold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Medium.ttf")
    }

    fun getBold(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Heavy.ttf")
    }

    fun getMedium(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Medium.ttf")
    }

    fun getThin(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Light.ttf")
    }

    fun getLight(context: Context?): Typeface {
        return Typeface.createFromAsset(context?.assets, "fonts/Avenir-Medium.ttf")
    }
}