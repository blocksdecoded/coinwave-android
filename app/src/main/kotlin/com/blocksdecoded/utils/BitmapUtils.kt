package com.blocksdecoded.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.ByteArrayOutputStream
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout

object BitmapUtils {
    private const val JPEG_QUALITY = 75
    private const val MAX_BITMAP_SIZE = 1024
    private const val THUMB_SIZE = 128
    private const val THUMB_QUALITY = 50
    private const val BITMAP_SCALE = 0.4f
    private const val BLUR_RADIUS = 7.5f

    fun compressBitmap(source: Bitmap): Bitmap = compress(source, MAX_BITMAP_SIZE)

    fun getThumb(source: Bitmap): Bitmap = compress(source, THUMB_SIZE)

    fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var image = image
        if (maxHeight > 0 && maxWidth > 0) {
            val width = image.width
            val height = image.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            return image
        } else {
            return image
        }
    }

    fun compress(source: Bitmap, size: Int): Bitmap{
        val outWidth: Int
        val outHeight: Int

        val inWidth = source.width
        val inHeight = source.height

        if (inWidth > inHeight){
            outWidth = size
            outHeight = (inHeight * size) / inWidth
        }else{
            outHeight = size
            outWidth = (inWidth * size) / inHeight
        }

        return Bitmap.createScaledBitmap(source, outWidth, outHeight, true)
    }

	fun getThumbJPEG(source: Bitmap, quality: Int = THUMB_QUALITY): ByteArray =
			bitmapToJPEG(compress(source, THUMB_SIZE), quality)

    fun bitmapToJPEG(source: Bitmap, quality: Int = JPEG_QUALITY): ByteArray {
        val stream = ByteArrayOutputStream()
        source.compress(Bitmap.CompressFormat.JPEG, quality, stream)

        return stream.toByteArray()
    }

    fun bitmapFromView(context: Context, view: View): Bitmap{
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        view.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.measure(DimenUtils.dpToPx(context, 64), DimenUtils.dpToPx(context, 64))
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, DimenUtils.dpToPx(context, 64), DimenUtils.dpToPx(context, 64))
        view.buildDrawingCache()

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

	fun getMapboxCenterIcon(context: Context, view: View): Bitmap {
		val source = bitmapFromView(context, view)
		return mapboxHeightHackIcon(source)
	}

	fun mapboxWidthHackIcon(icon: Bitmap): Bitmap {
		val loc_img = Bitmap.createBitmap(icon.width * 2, icon.height, Bitmap.Config.ARGB_8888)
		val bitmapCanvas = Canvas(loc_img)
		val tempBitmap = icon.copy(Bitmap.Config.ARGB_8888, false)
		bitmapCanvas.drawBitmap(tempBitmap, 0f, 0f, null)
		return loc_img
	}

	fun mapboxHeightHackIcon(icon: Bitmap): Bitmap {
		val loc_img = Bitmap.createBitmap(icon.width, icon.height * 2, Bitmap.Config.ARGB_8888)
		val bitmapCanvas = Canvas(loc_img)
		val tempBitmap = icon.copy(Bitmap.Config.ARGB_8888, false)
		bitmapCanvas.drawBitmap(tempBitmap, 0f, 0f, null)
		return loc_img
	}

    fun blurBitmap(image: Bitmap, context: Context): Bitmap{
        val width = Math.round(image.width * BITMAP_SCALE)
        val height = Math.round(image.height * BITMAP_SCALE)

        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
//        val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
//        theIntrinsic.setRadius(BLUR_RADIUS)
//        theIntrinsic.setInput(tmpIn)
//        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }
}