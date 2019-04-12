package com.blocksdecoded.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun focusView(context: Context?, view: View?, toggleKeyboard: Boolean = true) {
        try {
            if (context != null && context is Activity && view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                if (imm is InputMethodManager) {
                    view.requestFocus()
                    if (toggleKeyboard) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
                    } else {
                        imm.showSoftInput(view, 0)
                    }
                }
            }
        } catch (e: Exception) {
            logE(e)
        }
    }

    fun bindToKeyboard(context: Context?, view: View?) {
        if (context != null && context is Activity) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            if (imm is InputMethodManager) {
                view?.requestFocus()
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }
    }

    fun hideKeyboard(context: Context?) {
        if (context != null && context is Activity) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            if (imm is InputMethodManager && context.currentFocus != null) {
                imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
            }
        }
    }
}