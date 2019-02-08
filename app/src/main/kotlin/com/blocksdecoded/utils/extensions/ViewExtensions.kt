package com.blocksdecoded.utils.extensions

import android.view.View
import android.view.ViewGroup

/**
 * Created by askar on 2/1/19
 * with Android Studio
 */

fun View.setConstraintTopMargin(value: Int) = try {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.topMargin = value
    layoutParams = params
} catch (e: Exception) {

}