package com.alesh.library.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

object BitmapHelper {

    fun textToBitmap(text: String, paint: Paint, width: Int, height: Int): Bitmap {
        val baseline = -paint.ascent()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 0f, baseline, paint)
        return bitmap
    }

    fun rectToBitmap(rect: Rect, paint: Paint, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRect(rect, paint)
        return bitmap
    }
}