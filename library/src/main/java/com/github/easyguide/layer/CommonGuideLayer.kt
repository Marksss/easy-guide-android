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

    private fun onTargetClick(index: Int) {
        onLayerClickListener?.onClick(index, controller)
    }

    protected open fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    interface OnLayerClickListener {
        /**
         * @param targetIndex If targetIndex < 0, clicking outside targets area; If targetIndex >= 0, clicking inside targets area and targetIndex is the index of target that clicked
         * @param controller go to next layer or just dismiss current layer
         */
        fun onClick(targetIndex: Int, controller: ILayerController)
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
            override fun onClick(targetIndex: Int, controller: ILayerController) {
                controller.goNext()
            }
        }
    }
}
