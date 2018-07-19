package com.makeuseof.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.util.DisplayMetrics



object DimenUtils {
    fun dpToPx(context: Context, dp: Int): Int{
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun pxToDp(context: Context, px: Int): Int{
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }
	
	fun pxToSp(context: Context, px: Float): Float {
		val scaledDensity = context.resources.displayMetrics.scaledDensity
		return px / scaledDensity
	}
	
	fun spToPx(context: Context, sp: Float): Float{
		val scaledDensity = context.resources.displayMetrics.scaledDensity
		return sp * scaledDensity
	}
	
	fun getScreenHeight(context: Context): Int{
		return getScreenSize(context).y
	}
	
	fun getScreenWidth(context: Context): Int{
		return getScreenSize(context).x
	}
	
	fun getScreenSize(context: Context): Point{
		val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display = wm.defaultDisplay
		var size = Point()
		display.getSize(size)
		return size
	}
	
	fun getScreenInfo(context: Context): MetricsInfo {
		val metrics = DisplayMetrics()
		val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		wm.defaultDisplay.getMetrics(metrics)
		
		return MetricsInfo(
				metrics.heightPixels,
				metrics.widthPixels,
				metrics.density,
				metrics.densityDpi
		)
	}
	
	class MetricsInfo(
			var height: Int,
	        var width: Int,
	        var density: Float,
	        var densityDpi: Int
	)
}