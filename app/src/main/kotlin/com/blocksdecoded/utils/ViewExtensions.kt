package com.blocksdecoded.utils

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.ShapeDrawable
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.annotation.DimenRes
import androidx.annotation.ColorRes
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import androidx.core.view.ViewCompat.LAYER_TYPE_SOFTWARE
import android.view.animation.DecelerateInterpolator
import android.util.Log

// Created by askar on 5/31/18.

private val DEFAULT_ANIMATION_DURATION = 400

fun View.generateBackgroundWithShadow(
        @ColorRes backgroundColor: Int,
        @DimenRes cornerRadius: Int,
        @ColorRes shadowColor: Int,
        @DimenRes elevation: Int,
        shadowGravity: Int): Drawable {
    val cornerRadiusValue = this.context.resources.getDimension(cornerRadius)
    val elevationValue = this.context.resources.getDimension(elevation).toInt()
    val shadowColorValue = ContextCompat.getColor(this.context, shadowColor)
    val backgroundColorValue = ContextCompat.getColor(this.context, backgroundColor)

    val outerRadius = floatArrayOf(
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue,
            cornerRadiusValue
    )

    val backgroundPaint = Paint()
    backgroundPaint.style = Paint.Style.FILL
    backgroundPaint.setShadowLayer(cornerRadiusValue, 0f, 0f, 0)

    val shapeDrawablePadding = Rect()
    val defMulti = 1
    shapeDrawablePadding.left = elevationValue * defMulti
    shapeDrawablePadding.right = elevationValue * defMulti

    val DY: Int
    when (shadowGravity) {
        Gravity.CENTER -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue
            DY = 0
        }
        Gravity.TOP -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue
            DY = -1 * elevationValue / 3
        }
        Gravity.BOTTOM -> {
            shapeDrawablePadding.top = elevationValue * defMulti
            shapeDrawablePadding.bottom = elevationValue * (defMulti + 1)
            DY = elevationValue / 3
        }
        else -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue * 2
            DY = elevationValue / 3
        }
    }

    val shapeDrawable = ShapeDrawable()
    shapeDrawable.setPadding(shapeDrawablePadding)

    shapeDrawable.paint.color = backgroundColorValue
    shapeDrawable.paint.setShadowLayer(cornerRadiusValue / 3, 0f, DY.toFloat(), shadowColorValue)

    this.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

    shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

    val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
    val insetMulti = 3
    drawable.setLayerInset(0, elevationValue * insetMulti, elevationValue * (insetMulti + 1), elevationValue * insetMulti, elevationValue * (insetMulti + 1))

    this.background = drawable
    return drawable
}

fun View.animateAlpha(
        from: Float = -1f,
        to: Float,
        duration: Int = DEFAULT_ANIMATION_DURATION,
        onEnd: (() -> Unit)? = null
){
    this.clearAnimation()
    if (from >= 0f){
        alpha = from
    }
    animate()
            .alpha(to)
            .setDuration(duration.toLong())
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    onEnd?.invoke()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            .start()
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.removeShiftMode(){
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

