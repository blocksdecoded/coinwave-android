package com.makeuseof.utils.io

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

// Created by askar on 6/8/18.
class IOService(context: Context): IOContract {

    private var assets: AssetManager? = null
    private var filesDir: File? = null

    init {
        assets = context.assets
        filesDir = context.filesDir
    }

    companion object {
        private var INSTANCE: IOService? = null

        fun getInstance(context: Context): IOContract{
            if (INSTANCE == null)
                INSTANCE = IOService(context)
            return INSTANCE!!
        }

        fun destroyInstance(){
            INSTANCE?.assets = null
            INSTANCE = null
        }
    }

    private fun loadBytes(name: String): ByteArray? = try {
        val `is` = assets?.open(name)
        val size = `is`?.available()
        var buffer: ByteArray? = null
        if (size != null){
            buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
        }
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
    } catch (e: Exception){
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