package com.alesh.library.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

fun String.toBitmap(width: Int, height: Int, paint: Paint): Bitmap {
    val baseline = -paint.ascent()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawText(this, 0f, baseline, paint)
    return bitmap
}