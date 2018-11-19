package com.github.easyguide.layer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by shenxl on 2018/8/16.
 */

class GuideLayerView : RelativeLayout {
    var baseColor = 0x60000000
    private val targetRects = mutableListOf<Rect>()
    private val paint = Paint()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    internal var drawCallBack: ((id: Int, rect: Rect, canvas: Canvas, paint: Paint) -> Unit)? = null
    internal var targetClickListener: ((id: Int) -> Unit)? = null
    private var downX = 0f
    private var downY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec), View.MeasureSpec.EXACTLY))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = this.childCount
        for (i in 0 until childCount) {
            val child = this.getChildAt(i)
            val location = child.tag as Location
            if (location.index >= targetRects.size) {
                continue
            }
            val targetRect = targetRects[location.index]
            val verticalAxis = (targetRect.left + targetRect.right) / 2
            val horizontalAxis = (targetRect.top + targetRect.bottom) / 2
            val width = child.measuredWidth
            val height = child.measuredHeight
            when (location) {
                Location.TO_TOP -> child.layout(verticalAxis - width / 2, targetRect.top - height, verticalAxis + width / 2, targetRect.top)
                Location.TO_BOTTOM -> child.layout(verticalAxis - width / 2, targetRect.bottom, verticalAxis + width / 2, targetRect.bottom + height)
                Location.TO_LEFT -> child.layout(targetRect.left - width, horizontalAxis - height / 2, targetRect.left, horizontalAxis + height / 2)
                Location.TO_RIGHT -> child.layout(targetRect.right, horizontalAxis - height / 2, targetRect.right + width, horizontalAxis + height / 2)
                else -> child.layout(targetRect.left, targetRect.top, targetRect.right, targetRect.bottom)
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawColor(baseColor)
        paint.reset()
        paint.isAntiAlias = true
        paint.xfermode = xfermode
        drawCallBack?.let {
            for ((index, value) in targetRects.withIndex()) {
                it.invoke(index, value, canvas, paint)
            }
        }
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                val upX = event.x
                val upY = event.y
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {
                    targetClickListener?.let {
                        for ((index, value) in targetRects.withIndex()) {
                            if (contains(value, upX, upY)) {
                                it.invoke(index)
                                return true
                            }
                        }
                        it.invoke(NO_ID)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    internal fun addTargetsRect(rect: Rect) {
        targetRects.add(rect)
    }

    private fun contains(rect: Rect, x: Float, y: Float): Boolean {
        return (rect.left < rect.right && rect.top < rect.bottom
                && x >= rect.left && x < rect.right && y >= rect.top && y < rect.bottom)
    }
}
