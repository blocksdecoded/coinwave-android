package com.blocksdecoded.utils.extensions

import android.content.Context
import android.graphics.PorterDuff
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes

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

fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

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