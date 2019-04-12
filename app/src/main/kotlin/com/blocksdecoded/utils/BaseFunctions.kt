package com.blocksdecoded.utils

import android.content.Context
import android.widget.Toast

fun showShortToast(context: Context?, text: String) = showToast(context, text, Toast.LENGTH_SHORT)

fun showLongToast(context: Context?, text: String) = showToast(context, text, Toast.LENGTH_LONG)

private fun showToast(context: Context?, text: String, length: Int) {
    context?.let {
        Toast.makeText(it, text, length).show()
    }
}