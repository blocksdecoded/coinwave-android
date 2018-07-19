package com.makeuseof.utils

import android.content.Context
import android.os.Build

object ResourceUtil{
	fun getColor(context: Context, color: Int): Int{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			return context.resources.getColor(color, null)
		}else{
			return context.resources.getColor(color)
		}
	}
}