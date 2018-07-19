package com.makeuseof.utils

import android.animation.Animator
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.ShapeDrawable
import android.view.Gravity
import android.support.v4.content.ContextCompat
import android.support.annotation.DimenRes
import android.support.annotation.ColorRes
import android.view.View
import android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE
import android.view.animation.DecelerateInterpolator


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

