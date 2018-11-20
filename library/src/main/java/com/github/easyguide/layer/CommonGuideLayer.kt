package com.github.easyguide.layer

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import android.view.animation.Animation
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

class CommonGuideLayer(context: Context) : AbsGuideLayer() {
    private var targetCounts = 0
    private val layerView: GuideLayerView
    var enterAnimation: Animation? = null
    var exitAnimation: Animation? = null
    var onLayerClickListener: OnLayerClickListener? = defaultTargetClick
    var onHighLightDrawListener: OnHighLightDrawListener? = defaultHighlightDraw
    var backgroundColor: Int
        get() = layerView.baseColor
        set(value) {layerView.baseColor = value}

    init {
        layerView = GuideLayerView(context).apply {
            drawCallBack = this@CommonGuideLayer::onDraw
            targetClickListener = this@CommonGuideLayer::onTargetClick
        }
    }

    /**
     * @param view The target view that highlights
     */
    fun addHighlightTarget(view: View): CommonGuideLayer {
        targetCounts++
        view.post { addHighlightTarget(view.getViewAbsRect()) }
        return this
    }

    /**
     * @param rect Specific block that highlights
     */
    fun addHighlightTarget(rect: Rect): CommonGuideLayer {
        targetCounts++
        layerView.addTargetsRect(rect)
        return this
    }

    /**
     * Call right after {@link #addHighlightTarget()}, and the extra view's layout will be based on
     * the target that was added lately
     *
     * @param view Customized extra view around the target that highlights, such as pictures, texts
     * @param verticalOffset Offset in the vertical direction, offset>0 -> right, offset<0 -> left
     * @param horizontalOffset Offset in the vertical direction, offset>0 -> bottom, offset<0 -> top
     * @param locations Rules that orgnize the layout of extra view
     */
    fun withExtraView(view: View, verticalOffset: Int = 0, horizontalOffset: Int = 0, vararg locations: Location): CommonGuideLayer {
        layerView.addExtraView(view, targetCounts - 1, verticalOffset, horizontalOffset, locations.toList())
        return this
    }

    override fun makeView(context: Context): View {
        layerView.post {
            layerView.requestLayout()
        }
        return layerView
    }

    override fun onShow() {
        enterAnimation?.let { layerView.startAnimation(it) }
        super.onShow()
    }

    override fun onDismiss() {
        exitAnimation?.let { layerView.startAnimation(it) }
        super.onDismiss()
    }

    private fun onTargetClick(index: Int) {
        onLayerClickListener?.onClick(index, controller)
    }

    private fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        onHighLightDrawListener?.onDraw(index, rect, canvas, paint)
    }

    interface OnLayerClickListener {
        /**
         * Callback method to be invoked when layer is clicked
         * If targetIndex < 0, clicking outside targets area;
         * If targetIndex >= 0, clicking inside targets area and targetIndex is the index of target that clicked
         *
         * @param targetIndex
         * @param controller
         */
        fun onClick(targetIndex: Int, controller: ILayerController)
    }

    interface OnHighLightDrawListener {
        /**
         * Callback method used for customizing the shape of highlight area
         *
         * @param index The index of target that is added
         * @param rect
         * @param canvas
         * @param paint
         */
        fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint)
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

        private val defaultHighlightDraw = object : OnHighLightDrawListener {
            override fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
                canvas.drawRect(rect, paint)
            }
        }
    }
}
