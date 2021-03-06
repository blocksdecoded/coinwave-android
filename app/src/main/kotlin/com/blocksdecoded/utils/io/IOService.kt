package com.blocksdecoded.utils.io

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

// Created by askar on 6/8/18.
class IOService(context: Context) : IOContract {

    private var assets: AssetManager = context.assets
    private var filesDir: File = context.filesDir

    private fun loadBytes(name: String): ByteArray? = try {
        val `is` = assets.open(name)
        val size = `is`.available()
        val buffer: ByteArray?
        buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        buffer
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

    override fun loadJsonAsset(name: String): JSONObject? {
        try {
            val stringAsset = loadStringAsset(name)
            if (stringAsset.isNotEmpty()) return JSONObject(stringAsset)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return null
    }

    override fun loadStringAsset(name: String): String = try {
        loadBytes(name)?.let {
            return String(it, Charset.forName("UTF-8"))
        }

        ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    override fun loadByteAsset(name: String): ByteArray? = loadBytes(name)

    override fun saveBitmap(bitmap: Bitmap, path: String): String {
        try {
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}