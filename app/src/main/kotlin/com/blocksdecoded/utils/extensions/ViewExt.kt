package com.blocksdecoded.utils.extensions

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.annotation.AnimRes
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by askar on 2/1/19
 * with Android Studio
 */

private const val FAST_ANIM_DURATION = 100L
private const val SHORT_ANIM_DURATION = 200L
private const val MEDIUM_ANIM_DURATION = 400L

fun View.setConstraintTopMargin(value: Int) = try {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.topMargin = value
    layoutParams = params
} catch (e: Exception) {
}

fun View.playScaleAnimation(to: Float) {
    clearAnimation()

    val animation = ScaleAnimation(
        scaleX,
        to,
        scaleY,
        to,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )

    animation.duration = FAST_ANIM_DURATION
    animation.fillAfter = false

    this.startAnimation(animation)
}

fun View.playAnimation(@AnimRes id: Int) {
    val anim = AnimationUtils.loadAnimation(this.context, id)
    this.startAnimation(anim)
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.removeShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShifting(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
    }
}