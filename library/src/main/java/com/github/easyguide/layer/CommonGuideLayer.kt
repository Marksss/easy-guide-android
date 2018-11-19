package com.github.easyguide.layer

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

open class CommonGuideLayer(protected val context: Context) : AbsGuideLayer() {
    var onLayerClickListener: OnLayerClickListener? = defaultTargetClick
    var onMultiTargetsClickListener: OnMultiTargetsClickListener ? = null
    private var targetCounts: Int = 0

    private val viewContainer: GuideLayerView by lazy {
        GuideLayerView(context).apply {
            drawCallBack = this@CommonGuideLayer::onDraw
            targetClickListener = this@CommonGuideLayer::onTargetClick
        }
    }

    fun addTargetView(view: View): CommonGuideLayer {
        targetCounts++
        view.post { addTargetView(view.getViewAbsRect()) }
        return this
    }

    fun addTargetView(rect: Rect): CommonGuideLayer {
        targetCounts++
        viewContainer.addTargetsRect(rect)
        return this
    }

    fun withExtraView(view: View, verticalOffset: Int = 0, horizontalOffset: Int = 0, vararg locations: Location): CommonGuideLayer {
        viewContainer.addExtraView(view, targetCounts - 1, verticalOffset, horizontalOffset, locations.toList())
        return this
    }

    protected open fun onViewCreated(context: Context) {
    }

    final override fun makeView(context: Context): View {
        onViewCreated(context)
        viewContainer.post {
            viewContainer.requestLayout()
        }
        return viewContainer
    }

    private fun onTargetClick(index: Int){
        onMultiTargetsClickListener?.onClick(index, controller) ?: run {
            val type = if (index < 0) ClickType.OUTSIDE_TARGET else ClickType.ON_TARGET
            onLayerClickListener?.onClick(type, controller)
        }
    }

    protected open fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    interface OnLayerClickListener {
        fun onClick(type: ClickType, controller: ILayerController)
    }

    interface OnMultiTargetsClickListener {
        fun onClick(index: Int, controller: ILayerController)
    }

    enum class ClickType {
        ON_TARGET, OUTSIDE_TARGET;
    }

    companion object {
        private fun getStatusBarHeight(context: Context): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }

        fun View.getViewAbsRect(): Rect {
            val locView = IntArray(2)
            getLocationInWindow(locView)
            return Rect().apply {
                set(locView[0], locView[1], locView[0] + measuredWidth, locView[1] + measuredHeight)
                offset(0, if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) -getStatusBarHeight(context) else 0)
            }
        }

        private val defaultTargetClick = object : OnLayerClickListener {
            override fun onClick(type: ClickType, controller: ILayerController) {
                controller.goNext()
            }
        }
    }
}
