package com.alesh.library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getStringOrThrow
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.alesh.library.helpers.BitmapHelper.textToBitmap
import com.alesh.library.helpers.DisplayMetricsHelper.convertDpToPx
import com.alesh.library.helpers.TextMeasureHelper
import kotlin.math.max

class PorterDuffSwitch(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var isInitialLaunch: Boolean = true

    /* Retrieved attributes */
    private var leftText: String = ""
    private var rightText: String = ""
    private var indent: Int = 0
    private var startPosition = 0 // поменять имя

    /* Default attributes */
    private val defIndent = convertDpToPx(context, 8f).toInt()
    private val defXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP) // DST_ATOP

    /* Paints */
    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var switchPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Text measure helpers */
    private lateinit var allTextOpt: TextMeasureHelper
    private lateinit var leftTextOpt: TextMeasureHelper
    private lateinit var rightTextOpt: TextMeasureHelper

    /* Objects to paint */
    private lateinit var leftTextBitmap: Bitmap
    private lateinit var rightTextBitmap: Bitmap
    private lateinit var switchRect: Rect

    /* Points */
    private var pointLeft: Float = 0f
    private var pointRight: Float = 0f
    private var currentSwitchPosition: Float = 0f

    private var percent = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            percent = it.animatedValue as Float
        }
    }

    init {

        /* Disable hardware acceleration (аппаратное ускорение) */
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        /* Setup attributes and tools */
        retrieveAttributes(attrs)
        setupTextMeasureHelpers()
        setupAnimation()
        setupPoints()
    }

    private fun retrieveAttributes(attrs: AttributeSet) {

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.PorterDuffSwitch, 0, 0
        )

        val fontId: Int

        typedArray.let {
            leftText = it.getStringOrThrow(R.styleable.PorterDuffSwitch_pds_leftText)
            rightText = it.getStringOrThrow(R.styleable.PorterDuffSwitch_pds_rightText)
            indent = it.getDimensionPixelSize(R.styleable.PorterDuffSwitch_pds_indent, defIndent)
            startPosition = it.getInt(R.styleable.PorterDuffSwitch_pds_defaultPosition, 0)
            fontId = it.getResourceId(R.styleable.PorterDuffSwitch_pds_fontFamily, 0)
        }

        textPaint.apply {
            color = typedArray.getColorOrThrow(R.styleable.PorterDuffSwitch_pds_inactiveColor)
            typeface = fontId.takeIf { it != 0 }?.let { ResourcesCompat.getFont(context, it) }
            textSize = typedArray.getDimensionOrThrow(R.styleable.PorterDuffSwitch_pds_textSize)
            xfermode = defXfermode
        }

        switchPaint.apply {
            color = typedArray.getColorOrThrow(R.styleable.PorterDuffSwitch_pds_activeColor)
        }

        typedArray.recycle()
    }

    private fun setupTextMeasureHelpers() {
        allTextOpt = TextMeasureHelper(leftText + rightText, textPaint)
        leftTextOpt = TextMeasureHelper(leftText, textPaint)
        rightTextOpt = TextMeasureHelper(rightText, textPaint)
    }

    // CustomSpringInterpolator(0.6f) | DecelerateInterpolator() | FastOutSlowInInterpolator()
    private fun setupAnimation() {
        animator.apply {
            interpolator = LinearInterpolator()
            duration = 400
        }
    }

    private fun setupPoints() {
        pointLeft = leftTextOpt.width - max(leftTextOpt.width, rightTextOpt.width).toFloat()
        pointRight = leftTextOpt.width + indent.toFloat()
        currentSwitchPosition = if (startPosition == 0) pointLeft else pointRight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(allTextOpt.width + indent, allTextOpt.height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        leftTextBitmap = textToBitmap(leftText, textPaint, leftTextOpt.width + indent, height)
        rightTextBitmap = textToBitmap(rightText, textPaint, rightTextOpt.width, height)
        switchRect = Rect(0, 0, max(leftTextOpt.width, rightTextOpt.width), height)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSwitchRect(canvas)
        drawTexts(canvas)
        //scaleText(canvas)
    }

    private fun drawSwitchRect(canvas: Canvas?) {

        val futurePosition =
            if (startPosition == 0) lerp(currentSwitchPosition, pointRight, percent)
            else lerp(currentSwitchPosition, pointLeft, percent)

        canvas?.withTranslation(currentSwitchPosition, 0f) { drawRect(switchRect, switchPaint) }

        currentSwitchPosition = futurePosition
    }

    private fun drawTexts(canvas: Canvas?) {
        val leftStartPoint = 0
        val rightStartPoint = leftTextOpt.width + indent

//        if (startPosition == 0) {
//            canvas?.withScale(
//                lerp(1f, 0.9f, percent *2),
//                lerp(1f, 0.9f, percent * 2),
//                leftTextOpt.width / 2f,
//                leftTextOpt.height / 2f
//
//            ) {
//                drawBitmap(leftTextBitmap, leftStartPoint.toFloat(), 0f, textPaint)
//            }
//
//            canvas?.withScale(
//                lerp(0.9f, 1f, percent),
//                lerp(0.9f, 1f, percent),
//                rightTextOpt.width / 2f,
//                rightTextOpt.height / 2f
//
//            ) {
//                drawBitmap(rightTextBitmap, rightStartPoint.toFloat(), 0f, textPaint)
//            }
//        } else {
//
//            canvas?.withScale(
//                lerp(0.9f, 1f, percent),
//                lerp(0.9f, 1f, percent),
//                leftTextOpt.width / 2f,
//                leftTextOpt.height / 2f
//
//            ) {
//                drawBitmap(leftTextBitmap, leftStartPoint.toFloat(), 0f, textPaint)
//            }
//
//            canvas?.withScale(
//                lerp(1f, 0.9f, percent),
//                lerp(1f, 0.9f, percent),
//                rightTextOpt.width / 2f,
//                rightTextOpt.height / 2f
//
//            ) {
//                drawBitmap(rightTextBitmap, rightStartPoint.toFloat(), 0f, textPaint)
//            }
//        }

        canvas?.drawBitmap(leftTextBitmap, leftStartPoint.toFloat(), 0f, textPaint)
        canvas?.drawBitmap(rightTextBitmap, rightStartPoint.toFloat(), 0f, textPaint)
    }

    private fun scaleText(canvas: Canvas?) {

        canvas?.withScale(
            lerp(1f, 0.3f, percent),
            lerp(1f, 0.3f, percent),
            (leftTextOpt.width / 2).toFloat(),
            (leftTextOpt.height / 2).toFloat()

        ) {
            drawBitmap(leftTextBitmap, 0f, 0f, textPaint)
        }
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }

    fun switch() {
        animator.start()
        if (isInitialLaunch) isInitialLaunch = false
        else startPosition = if (startPosition == 0) 1 else 0
    }
}