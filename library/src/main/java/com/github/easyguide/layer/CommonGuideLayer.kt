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
    private var targetCounts: Int = 0
    private val viewContainer: GuideLayerView
    var onLayerClickListener: OnLayerClickListener? = defaultTargetClick
    var backgroundColor: Int
        get() = viewContainer.baseColor
        set(value) {viewContainer.baseColor = value}

    init {
        viewContainer = GuideLayerView(context).apply {
            drawCallBack = this@CommonGuideLayer::onDraw
            targetClickListener = this@CommonGuideLayer::onTargetClick
        }
    }

    /**
     * @param view The target view that highlights
     */
    fun addTargetView(view: View): CommonGuideLayer {
        targetCounts++
        view.post { addTargetView(view.getViewAbsRect()) }
        return this
    }

    /**
     * @param rect The area that highlights
     */
    fun addTargetView(rect: Rect): CommonGuideLayer {
        targetCounts++
        viewContainer.addTargetsRect(rect)
        return this
    }

    /**
     * Called right after {@link #addTargetView()}, and the extra view's layout will be based on
     * the target that was added lately
     *
     * @param view Customized extra view around the target that highlights, such as pictures, texts
     * @param verticalOffset Offset in the vertical direction, offset>0 -> right, offset<0 -> left
     * @param horizontalOffset Offset in the vertical direction, offset>0 -> bottom, offset<0 -> top
     * @param locations Rules that orgnize the layout of extra view
     */
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

    /**
     * Used for customizing the shape of highlight area when rewrote
     *
     * @param index The index of target that is added
     * @param rect
     * @param canvas
     * @param paint
     */
    protected open fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    interface OnLayerClickListener {
        /**
         * If targetIndex < 0, clicking outside targets area;
         * If targetIndex >= 0, clicking inside targets area and targetIndex is the index of target that clicked
         * @param targetIndex
         * @param controller
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
