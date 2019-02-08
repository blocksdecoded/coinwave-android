package com.blocksdecoded.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.webkit.WebView

// Created by askar on 6/21/18.
fun WebView.getBitmap(): Bitmap {
    val picture = capturePicture()
    val bitmap = Bitmap.createBitmap(
            if (picture.width > 0) picture.width else 1,
            if (picture.height > 0) picture.height else 1,
            Bitmap.Config.RGB_565
    )
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    picture.draw(canvas)
    return bitmap
}