package com.blocksdecoded.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun focusView(context: Context?, view: View?, toggleKeyboard: Boolean = true) {
        try {
            if (context != null && context is Activity && view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                view.requestFocus()
                if (toggleKeyboard) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
                } else {
                    imm.showSoftInput(view, 0)
                }
            }
        } catch (e: Exception) {
            Lg.d(e.message)
        }
    }

    fun bindToKeyboard(context: Context?, view: View?) {
        if (context != null && context is Activity) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view?.requestFocus()
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    fun hideKeyboard(context: Context?) {
        if (context != null && context is Activity) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (context.currentFocus != null) {
                imm.hideSoftInputFromWindow(context.currentFocus.windowToken, 0)
            }
        }
    }
}