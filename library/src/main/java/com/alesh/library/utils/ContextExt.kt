package com.alesh.library.utils

import android.content.Context

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp - A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Context.dpToPx(dp: Float): Float = dp * resources.displayMetrics.density

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px - A value in px (pixels) unit. Which we need to convert into db
 * @return A float value to represent dp equivalent to px value
 */
fun Context.pxToDp(px: Float): Float = px / resources.displayMetrics.density