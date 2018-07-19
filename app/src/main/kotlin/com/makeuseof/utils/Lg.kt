package com.makeuseof.utils

import android.util.Log
import com.makeuseof.cryptocurrency.BuildConfig

// Created by askar on 5/25/18.

object Lg{
    fun d(text: String?, tag: String = "ololo"){
        if (BuildConfig.DEBUG){
            text?.let{ Log.d(tag, it) }
        }
    }
}