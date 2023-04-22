package com.anubhav.checkboxplayground.checkBox

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.withStyledAttributes
import com.anubhav.checkboxplayground.R

class AstroCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.checkboxStyle
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    private var curveRadius: Float = 0.0f
    private var borderWidth: Float = 0.0f
    private var tickWidth: Float = 0.0f
    private var tickColor: Int = 0
    private var bgColorChecked: Int = 0
    private var bgColorUnchecked: Int = 0
    private var borderColorChecked: Int = 0
    private var borderColorUnchecked: Int = 0

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val tickPath = Path()
    private val boxRect = RectF()

    private val tickAnimator: ValueAnimator
    private var tickProgress = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.AstroCheckBox) {
            curveRadius = getDimension(R.styleable.AstroCheckBox_curveRadius, 10f)
            borderWidth = getDimension(R.styleable.AstroCheckBox_borderWidth, 2f)
            tickWidth = getDimension(R.styleable.AstroCheckBox_tickWidth, 2f)
            tickColor = getColor(R.styleable.AstroCheckBox_tickColor, Color.BLACK)
            bgColorChecked = getColor(R.styleable.AstroCheckBox_bgColorChecked, Color.GRAY)
            bgColorUnchecked = getColor(R.styleable.AstroCheckBox_bgColorUnchecked, Color.WHITE)
            borderColorChecked = getColor(R.styleable.AstroCheckBox_borderColorChecked, Color.BLACK)
            borderColorUnchecked = getColor(R.styleable.AstroCheckBox_borderColorUnchecked, Color.BLACK)
        }

        // Configure paint objects
        borderPaint.strokeWidth = borderWidth
        tickPaint.strokeWidth = tickWidth
        tickPaint.color = tickColor

        // Set up the tick animation
        tickAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener { animation ->
                tickProgress = animation.animatedValue as Float
            }
        }

        // Set up the listener for checking/unchecking the CheckBox
        setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                tickAnimator.start()
            }else{
                tickAnimator.reverse()
            }
            onAstroCheckBoxSelected?.invoke(this, isChecked)
        }
    }

    var onAstroCheckBoxSelected: ((AstroCheckBox, Boolean) -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawBorder(canvas)
        drawTick(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        backgroundPaint.color = if (isChecked) bgColorChecked else bgColorUnchecked
        boxRect.set(
            borderWidth / 2,
            borderWidth / 2,
            width - borderWidth / 2,
            height - borderWidth / 2
        )
        canvas.drawRoundRect(boxRect, curveRadius, curveRadius, backgroundPaint)
    }

    private fun drawBorder(canvas: Canvas) {
        borderPaint.color = if (isChecked) borderColorChecked else borderColorUnchecked
        canvas.drawRoundRect(boxRect, curveRadius, curveRadius, borderPaint)
    }

    private fun drawTick(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        tickPath.reset()
        tickPath.moveTo(width * 0.25f, height * 0.5f)
        tickPath.lineTo(width * 0.45f, height * 0.7f)
        tickPath.lineTo(width * 0.75f, height * 0.3f)

        val tickBounds = RectF()
        tickPath.computeBounds(tickBounds, true)

        val segment = tickPath.measurePathLength() * tickProgress
        val tickMeasure = tickPath.createPathMeasure()
        val tickSegment = Path()

        tickMeasure.getSegment(0f, segment, tickSegment, true)

        canvas.drawPath(tickSegment, tickPaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

    private fun Path.measurePathLength(): Float {
        val pathMeasure = PathMeasure(this, false)
        return pathMeasure.length
    }

    private fun Path.createPathMeasure(): PathMeasure {
        return PathMeasure(this, false)
    }
}


