package com.alesh.library.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

object BitmapHelper {

    fun textToBitmap(text: String, paint: Paint, width: Int, height: Int): Bitmap {
        val baseline = -paint.ascent()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 0f, baseline, paint)
        return bitmap
    }
}