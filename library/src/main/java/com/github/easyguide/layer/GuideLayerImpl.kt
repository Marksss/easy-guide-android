package com.github.easyguide.layer

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

class GuideLayerImpl private constructor(container: FrameLayout): IGuideLayer {
    override var parentView: FrameLayout = container
    override lateinit var controller: ILayerController
    override var next: IGuideLayer? = null

    private var targetCounts = 0
    private val layerView: GuideLayerView
    var enterAnimation: Animation? = null
    var exitAnimation: Animation? = null
    var onLayerClickListener: OnLayerClickListener? = defaultTargetClick
    var onHighLightDrawListener: OnHighLightDrawListener? = null
    private var defaultDraw = true
    var onDismissListener: OnLayerDismissListener? = null
    var onShowListener: OnLayerShowListener? = null
    var backgroundColor: Int
        get() = layerView.baseColor
        set(value) {layerView.baseColor = value}

    init {
        layerView = GuideLayerView(container.context).apply {
            drawCallBack = this@GuideLayerImpl::onDraw
            targetClickListener = this@GuideLayerImpl::onTargetClick
        }
    }

    /**
     * @param view The target view that highlights
     */
    fun addHighlightTarget(view: View): GuideLayerImpl {
        targetCounts++
        view.post { addHighlightTarget(Location.getViewAbsRect(view)) }
        return this
    }

    /**
     * @param rect Specific block that highlights
     */
    fun addHighlightTarget(rect: Rect): GuideLayerImpl {
        targetCounts++
        layerView.addTargetsRect(rect)
        return this
    }

    /**
     * Call right after {@link #addHighlightTarget()}, and the extra view's layout will be based on
     * the target that was added lately
     *
     * @param view Customized extra view around the target that highlights, such as pictures, texts
     * @param verticalOffset Offset(px) in the vertical direction, offset>0 -> right, offset<0 -> left
     * @param horizontalOffset Offset(px) in the vertical direction, offset>0 -> bottom, offset<0 -> top
     * @param locations Rules that orgnize the layout of extra view
     */
    fun withExtraView(view: View, verticalOffset: Int = 0, horizontalOffset: Int = 0, vararg locations: Location): GuideLayerImpl {
        layerView.addExtraView(view, targetCounts - 1, verticalOffset, horizontalOffset, locations.toList())
        return this
    }

    fun disableDefaultDraw() {
        defaultDraw = false
    }

    override fun getView(context: Context): View {
        return layerView
    }

    override fun onShow() {
        enterAnimation?.let { layerView.startAnimation(it) }
        onShowListener?.onShow()
    }

    override fun onDismiss() {
        exitAnimation?.let { layerView.startAnimation(it) }
        onDismissListener?.onDismiss()
    }

    private fun onTargetClick(index: Int) {
        onLayerClickListener?.onClick(index, controller)
    }

    private fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        val offset = Location.getViewAbsRect(layerView)
        rect.offset(-offset.left, -offset.top)
        if (onHighLightDrawListener?.onDraw(index, rect, canvas, paint) != true && defaultDraw) {
            defaultHighlightDraw.onDraw(index, rect, canvas, paint)
        }
    }

    interface OnLayerClickListener {
        /**
         * Callback method to be invoked when layer is clicked
         * If targetIndex < 0, clicking outside targets area;
         * If targetIndex >= 0, clicking inside targets area and targetIndex is the index of target that is clicked
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
         * @return true：intercept drawing the default highlight area；
         *          false：show the highlight area by default
         */
        fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint): Boolean
    }

    interface OnLayerDismissListener {
        /**
         * Callback method to be invoked when the current layer dismisses
         */
        fun onDismiss()
    }

    interface OnLayerShowListener {
        /**
         * Callback method to be invoked when the current layer shows
         */
        fun onShow()
    }

    companion object {
        /**
         * Just add layers to the FrameLayout
         * @param parentView
         */
        fun coverFrameLayout(frameLayout: FrameLayout): GuideLayerImpl {
            return GuideLayerImpl(frameLayout)
        }

        /**
         * Add layers to decorView
         * @param activity
         */
        fun coverActivity(activity: Activity): GuideLayerImpl {
            return coverFrameLayout(activity.window.decorView as FrameLayout)
        }

        /**
         * Show layers on a dialog
         * @param dialog
         */
        fun coverDialog(dialog: Dialog): GuideLayerImpl {
            return coverFrameLayout(dialog.window.decorView as FrameLayout)
        }

        private val defaultTargetClick = object : OnLayerClickListener {
            override fun onClick(targetIndex: Int, controller: ILayerController) {
                controller.goNext()
            }
        }

        private val defaultHighlightDraw = object : OnHighLightDrawListener {
            override fun onDraw(index: Int, rect: Rect, canvas: Canvas, paint: Paint): Boolean {
                canvas.drawRect(rect, paint)
                return true
            }
        }
    }
}
