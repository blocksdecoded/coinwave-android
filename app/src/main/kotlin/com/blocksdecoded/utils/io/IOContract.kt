package com.blocksdecoded.utils.io

import android.graphics.Bitmap
import org.json.JSONObject

// Created by askar on 6/8/18.
interface IOContract {
    fun loadJsonAsset(name: String): JSONObject?

    fun loadStringAsset(name: String): String

    fun loadByteAsset(name: String): ByteArray?

    fun saveBitmap(bitmap: Bitmap, path: String): String
}