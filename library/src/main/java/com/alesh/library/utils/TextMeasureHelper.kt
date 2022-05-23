package com.alesh.library.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

class TextMeasureHelper(text: String, paint: TextPaint) {

    private val textLayout = createLayout(text, paint)

    var width: Int = 0
        get() = textLayout.width
        private set

    var height: Int = 0
        get() = textLayout.height
        private set

    private fun createLayout(text: String, paint: TextPaint): StaticLayout {
        val textWidth = paint.measureText(text).toInt()
        return text.let {
            if (SDK_INT >= M) StaticLayout.Builder.obtain(it, 0, it.length, paint, textWidth).build()
            else StaticLayout(text, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
        }
    }
}