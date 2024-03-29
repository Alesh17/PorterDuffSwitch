package com.alesh.library

import android.animation.TimeInterpolator
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
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getStringOrThrow
import androidx.core.graphics.withTranslation
import com.alesh.library.models.CurrentState
import com.alesh.library.models.CurrentState.LEFT
import com.alesh.library.models.CurrentState.RIGHT
import com.alesh.library.utils.TextMeasureHelper
import com.alesh.library.utils.dpToPx
import com.alesh.library.utils.toBitmap
import kotlin.math.max

class PorterDuffSwitch(context: Context, attrs: AttributeSet) : View(context, attrs), View.OnClickListener {

    /* Listeners */
    private var onChangeStateListener: OnStateChangeListener? = null

    /* Retrieved attributes */
    private var leftText: String = ""
    private var rightText: String = ""
    private var indent: Int = 0
    private var currentState: CurrentState = LEFT
    private var animatorInterpolator: TimeInterpolator = DecelerateInterpolator()
    private var animatorDuration: Long = 800

    /* Current lerp */
    private lateinit var currentLerp: () -> Float

    /* Default attributes */
    private val defActiveColor = resources.getColor(R.color.activeColor)
    private val defInactiveColor = resources.getColor(R.color.inactiveColor)
    private val defIndent = context.dpToPx(8f).toInt()
    private val defXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    /* Paints */
    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var switchPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Text measure helpers (for measure theirs width and height) */
    private lateinit var allTextOpt: TextMeasureHelper
    private lateinit var leftTextOpt: TextMeasureHelper
    private lateinit var rightTextOpt: TextMeasureHelper

    /* Objects to paint */
    private lateinit var leftTextBitmap: Bitmap
    private lateinit var rightTextBitmap: Bitmap
    private lateinit var switchRect: Rect

    /* Points */
    private var pointLeft: Float = 0f // left upper corner of switchRect in the left side
    private var pointRight: Float = 0f // left upper corner of switchRect in the right side
    private var currentSwitchPosition: Float = 0f // switchRect left upper corner's position

    /* Animation completion percentage */
    private var percent = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener { percent = it.animatedValue as Float }
    }

    init {

        /* Disable hardware acceleration (аппаратное ускорение) */
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        /* Setup attributes and tools */
        retrieveAttributes(attrs)
        setupTextMeasureHelpers()
        setupAnimation()
        setupPoints()

        /* Set initial lerp */
        setLerpByCurrentState()

        this.setOnClickListener(this)
    }

    private fun retrieveAttributes(attrs: AttributeSet) {

        val array = context.obtainStyledAttributes(attrs, R.styleable.PorterDuffSwitch, 0, 0)

        leftText = array.getStringOrThrow(R.styleable.PorterDuffSwitch_pds_textLeft)
        rightText = array.getStringOrThrow(R.styleable.PorterDuffSwitch_pds_textRight)
        indent = array.getDimensionPixelSize(R.styleable.PorterDuffSwitch_pds_indent, defIndent)
        currentState =
            if (array.getInt(R.styleable.PorterDuffSwitch_pds_defaultState, 0) == 0) LEFT
            else RIGHT

        val fontId = array.getResourceId(R.styleable.PorterDuffSwitch_pds_fontFamily, 0)

        textPaint.apply {
            color = array.getColor(R.styleable.PorterDuffSwitch_pds_colorInactive, defInactiveColor)
            typeface = fontId.takeIf { it != 0 }?.let { ResourcesCompat.getFont(context, it) }
            textSize = array.getDimensionOrThrow(R.styleable.PorterDuffSwitch_pds_textSize)
            xfermode = defXfermode
        }

        switchPaint.apply {
            color = array.getColor(R.styleable.PorterDuffSwitch_pds_colorActive, defActiveColor)
        }

        array.recycle()
    }

    private fun setupTextMeasureHelpers() {
        allTextOpt = TextMeasureHelper(leftText + rightText, textPaint)
        leftTextOpt = TextMeasureHelper(leftText, textPaint)
        rightTextOpt = TextMeasureHelper(rightText, textPaint)
    }

    private fun setupAnimation() {
        animator.apply {
            interpolator = animatorInterpolator
            duration = animatorDuration
        }
    }

    private fun setupPoints() {
        pointLeft = leftTextOpt.width - max(leftTextOpt.width, rightTextOpt.width).toFloat()
        pointRight = leftTextOpt.width + indent.toFloat()
        currentSwitchPosition = if (currentState.isLeft()) pointLeft else pointRight
    }

    override fun onClick(view: View?) {
        switch()
        onChangeStateListener?.onStateChanged(this, currentState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(allTextOpt.width + indent, allTextOpt.height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        leftTextBitmap = leftText.toBitmap(leftTextOpt.width + indent, height, textPaint)
        rightTextBitmap = rightText.toBitmap(rightTextOpt.width, height, textPaint)
        switchRect = Rect(0, 0, max(leftTextOpt.width, rightTextOpt.width), height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSwitchRect(canvas)
        drawTexts(canvas)
    }

    private fun drawSwitchRect(canvas: Canvas?) {
        currentSwitchPosition = currentLerp()
        canvas?.withTranslation(currentSwitchPosition, 0f) { drawRect(switchRect, switchPaint) }
    }

    private fun drawTexts(canvas: Canvas?) {
        canvas?.drawBitmap(leftTextBitmap, 0f, 0f, textPaint)
        canvas?.drawBitmap(rightTextBitmap, pointRight, 0f, textPaint)
    }

    /**
     * Linear interpolation function.
     * Just difference between two point multiplied on time (animation completion percentage in our case).
     */
    private fun lerp(startPoint: Float, endPoint: Float, percent: Float): Float {
        return startPoint + (endPoint - startPoint) * percent
    }

    /**
     * Since the lerp functions of moving left and right are different,
     * before each start of the animation we set the lerp function in a separate variable.
     */
    private fun setLerpByCurrentState() {
        currentLerp = if (currentState.isLeft()) {
            { lerp(currentSwitchPosition, pointRight, percent) }
        } else {
            { lerp(currentSwitchPosition, pointLeft, percent) }
        }
    }

    private fun switch() {
        setLerpByCurrentState()
        animator.start()
        currentState = currentState.switch()
    }

    /**
     * @return current state (LEFT or RIGHT) of PorterDuffSwitch.
     */
    fun getState() = currentState

    /**
     * Sets the default state of switch without animation.
     *
     * @param state The state for which you want to change switch state.
     */
    fun setDefaultState(state: CurrentState) {
        currentState = state
        setLerpByCurrentState()
        setupPoints()
    }

    /**
     * Sets state of switch in runtime with animation.
     *
     * @param state The state for which you want to change switch state.
     */
    fun setState(state: CurrentState) {
        if (currentState != state) switch()
    }

    /**
     * The time interpolator used in calculating the elapsed fraction of this animation. The
     * interpolator determines whether the animation runs with linear or non-linear motion,
     * such as acceleration and deceleration. The default value is
     * {@link android.view.animation.AccelerateDecelerateInterpolator}
     *
     * @param interpolator the interpolator to be used by this animation. A value of <code>null</code>
     * will result in linear interpolation.
     */
    fun setInterpolator(interpolator: TimeInterpolator) {
        animatorInterpolator = interpolator
        setupAnimation()
    }

    /**
     * Sets the length of the animation. The default duration is 800 milliseconds.
     *
     * @param duration The length of the animation, in milliseconds. This value cannot
     * be negative.
     */
    fun setDuration(duration: Long) {
        if (duration < 0) throw IllegalArgumentException("Animators cannot have negative duration: $duration")
        animatorDuration = duration
        setupAnimation()
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change.
     */
    fun setOnStateChangeListener(listener: OnStateChangeListener) {
        onChangeStateListener = listener
    }
}