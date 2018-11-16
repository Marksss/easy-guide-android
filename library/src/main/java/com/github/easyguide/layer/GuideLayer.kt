package com.github.easyguide.layer

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.view.View
import com.github.easyguide.client.ILayerCallback

/**
 * Created by shenxl on 2018/8/16.
 */

open class GuideLayer(protected val activity: Activity) : AbsGuideLayer() {
    private lateinit var viewContainer: GuideLayerView
    private val targetCache = mutableListOf<Rect>()
    private val viewCache = mutableListOf<View>()
    var fullScreenClickListener: OnFullScreenClickListener? = null
    var singleTargetClickListener: OnSingleTargetClickListener? = null
    var defaultClickEnable = true

    protected open fun onViewCreated(context: Context) {
    }

    fun addTargetView(view: View): GuideLayer {
        view.post {
            addTargetView(getViewAbsRect(activity, view))
        }
        return this
    }

    fun addTargetView(rect: Rect): GuideLayer {
        if (this::viewContainer.isInitialized) {
            viewContainer.addTargetsRect(rect)
            viewContainer.requestLayout()
        } else {
            targetCache.add(rect)
        }
        return this
    }

    fun addExtraView(view: View, location: Location, tartgetIndex: Int): GuideLayer {
        location.setIndex(tartgetIndex)
        view.tag = location
        if (this::viewContainer.isInitialized) {
            viewContainer.addView(view)
        } else {
            viewCache.add(view)
        }
        return this
    }

    override fun makeView(context: Context): View {
        viewContainer = GuideLayerView(context).apply {
            drawCallBack = this@GuideLayer::onDraw
            totalClickListener = this@GuideLayer::onFullClick
            singleClickListener = this@GuideLayer::onSingleClick
            for (item in targetCache) {
                addTargetsRect(item)
            }
            for (item in viewCache) {
                addView(item)
            }
        }
        onViewCreated(context)
        return viewContainer
    }

    protected open fun onDraw(id: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    private fun onFullClick() {
        if (singleTargetClickListener == null && defaultClickEnable) {
            fullScreenClickListener?.onClick(viewContainer, callback) ?: run {
                callback.dismissCurrent()
            }
        }
    }

    private fun onSingleClick(id: Int) {
        if (defaultClickEnable) {
            singleTargetClickListener?.onClick(id, viewContainer, callback)
        }
    }

    interface OnFullScreenClickListener {
        fun onClick(container: GuideLayerView, callback: ILayerCallback)
    }

    interface OnSingleTargetClickListener {
        fun onClick(id: Int, container: GuideLayerView, callback: ILayerCallback)
    }

    companion object {
        private fun getStatusBarHeight(context: Context): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }

        fun getViewAbsRect(context: Context, view: View): Rect {
            val locView = IntArray(2)
            view.getLocationInWindow(locView)
            val rect = Rect()
            rect.set(locView[0], locView[1], locView[0] + view.measuredWidth, locView[1] + view.measuredHeight)
            rect.offset(0, if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) -getStatusBarHeight(context) else 0)
            return rect
        }
    }
}
