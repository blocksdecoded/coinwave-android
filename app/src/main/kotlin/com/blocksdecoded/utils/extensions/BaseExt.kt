package com.blocksdecoded.utils.extensions

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.PorterDuff
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.ViewCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(context)
            .load(url)
            .into(this)
}

fun View?.visible() {
    this?.let {
        if (visibility != View.VISIBLE)
            this.visibility = View.VISIBLE
    }
}

fun View?.hide() {
    this?.let {
        if (visibility != View.GONE)
            this.visibility = View.GONE
    }
}

fun View?.invisible() {
    this?.let {
        if (visibility != View.INVISIBLE)
            this.visibility = View.INVISIBLE
    }
}

fun View?.enable() {
    this?.let {
        if (!isEnabled)
            this.isEnabled = true
    }
}

fun View?.disable() {
    this?.let {
        if (isEnabled) {
            this.isEnabled = false
        }
    }
}

val ViewGroup.children
    get() = (0 until childCount).map { getChildAt(it) }

fun ViewGroup?.inflate(layoutRes: Int): View? =
    this?.let { LayoutInflater.from(context).inflate(layoutRes, this, false) }

//region Move

fun View?.moveLeft(value: Float) {
    this?.let {
        it.x -= value
    }
}

fun View?.moveRight(value: Float) {
    this?.let {
        it.x += value
    }
}

fun View?.moveUp(value: Float) {
    this?.let {
        it.y += value
    }
}

fun View?.moveDown(value: Float) {
    this?.let {
        it.y -= value
    }
}

fun View.getColorRes(@ColorRes color: Int): Int = context.getColorRes(color)

//endregion

fun TextView.updateVisibility() = if (text.isNullOrEmpty()) {
    visibility = View.GONE
} else {
    visibility = View.VISIBLE
}

fun EditText?.setPlainText(text: String) {
    this?.setText(text, TextView.BufferType.EDITABLE)
}

fun ImageView?.setImageColor(resId: Int) {
    this?.drawable?.setColorFilter(getColorRes(resId), PorterDuff.Mode.SRC_ATOP)
}

fun <T> List<T>?.isValidIndex(index: Int): Boolean {
    this?.let { return index >= 0 && it.size > index }
    return false
}

fun ViewGroup?.getCollisionWith(event: MotionEvent): View? {
    // Declare the LocationBuffer.
    this?.let {
        val lLocationBuffer = IntArray(2)
        // Iterate the children.
        for (i in 0 until this.childCount) {
            // Fetch the child View.
            val lView = this.getChildAt(i)
            // Fetch the View's location.
            lView.getLocationOnScreen(lLocationBuffer)
            // Is the View colliding?
            if (event.rawX > lLocationBuffer[0] &&
                    event.rawX < lLocationBuffer[0] + lView.width && event.rawY > lLocationBuffer[1] &&
                    event.rawY < lLocationBuffer[1] + lView.height) {
                // Return the colliding View.
                return lView
            }
        }
    }
    // We couldn't find a colliding View.
    return null
}

fun View.enableChangingTransition() {
    if (this is ViewGroup) {
        val layoutTransition = this.layoutTransition
        layoutTransition?.let {
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }
    }
}

fun Snackbar.config(context: Context): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val defMargin = context.dpToPx(6)
    params.setMargins(defMargin, defMargin, defMargin, defMargin)
    this.view.layoutParams = params

    ViewCompat.setElevation(this.view, 8f)
    return this
}
