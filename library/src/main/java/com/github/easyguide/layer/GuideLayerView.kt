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
    internal var drawCallBack: ((index: Int, rect: Rect, canvas: Canvas, paint: Paint) -> Unit)? = null
    internal var targetClickListener: ((index: Int) -> Unit)? = null
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
            val locBean = child.tag as LocBean
            if (locBean.targetIndex !in 0 until targetRects.size) {
                continue
            }
            val targetRect = targetRects[locBean.targetIndex]
            val verticalAxis = (targetRect.left + targetRect.right) / 2
            val horizontalAxis = (targetRect.top + targetRect.bottom) / 2
            val width = child.measuredWidth
            val height = child.measuredHeight
            var top = 0
            var bottom = 0
            var left = 0
            var right = 0
            locBean.locs.forEach {
                when (it) {
                    Location.TO_TOP -> {
                        left = if (left == 0) verticalAxis - width / 2 else left
                        top = targetRect.top - height
                        right = if (right == 0) verticalAxis + width / 2 else right
                        bottom = targetRect.top
                    }
                    Location.TO_BOTTOM -> {
                        left = if (left == 0) verticalAxis - width / 2 else left
                        top = targetRect.bottom
                        right = if (right == 0) verticalAxis + width / 2 else right
                        bottom = targetRect.bottom + height
                    }
                    Location.TO_LEFT -> {
                        left = targetRect.left - width
                        top = if (top == 0) horizontalAxis - height / 2 else top
                        right = targetRect.left
                        bottom = if (bottom == 0) horizontalAxis + height / 2 else bottom
                    }
                    Location.TO_RIGHT -> {
                        left = targetRect.right
                        top = if (top == 0) horizontalAxis - height / 2 else top
                        right = targetRect.right + width
                        bottom = if (bottom == 0) horizontalAxis + height / 2 else bottom
                    }
                    Location.COVER -> {
                        left = targetRect.left
                        top = targetRect.top
                        right = targetRect.right
                        bottom = targetRect.bottom
                    }
                    Location.ALIGN_TOP -> {
                        left = if (left == 0) verticalAxis - width / 2 else left
                        top = targetRect.top
                        right = if (right == 0) verticalAxis + width / 2 else right
                        bottom = targetRect.top + height
                    }
                    Location.ALIGN_BOTTOM -> {
                        left = if (left == 0) verticalAxis - width / 2 else left
                        top = targetRect.bottom - height
                        right = if (right == 0) verticalAxis + width / 2 else right
                        bottom = targetRect.bottom
                    }
                    Location.ALIGN_LEFT -> {
                        left = targetRect.left
                        top = if (top == 0) horizontalAxis - height / 2 else top
                        right = targetRect.left + width
                        bottom = if (bottom == 0) horizontalAxis + height / 2 else bottom
                    }
                    Location.ALIGN_RIGHT -> {
                        left = targetRect.right - width
                        top = if (top == 0) horizontalAxis - height / 2 else top
                        right = targetRect.right
                        bottom = if (bottom == 0) horizontalAxis + height / 2 else bottom
                    }
                }
            }
            child.layout(left + locBean.horizontalOffset,
                    top + locBean.verticalOffset,
                    right + locBean.horizontalOffset,
                    bottom + locBean.verticalOffset)
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
                            if (value.contains(upX, upY)) {
                                it.invoke(index)
                                return true
                            }
                        }
                        it.invoke(-1)
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

    internal fun addExtraView(view: View, targetIndex: Int, verticalOffset: Int, horizontalOffset: Int, locs: List<Location>) {
        view.tag = LocBean(targetIndex, locs, verticalOffset, horizontalOffset)
        addView(view)
    }

    private fun Rect.contains(x: Float, y: Float): Boolean {
        return (left < right && top < bottom && x >= left && x < right && y >= top && y < bottom)
    }

    private data class LocBean(var targetIndex: Int, var locs: List<Location>, var verticalOffset: Int, var horizontalOffset: Int)
}
