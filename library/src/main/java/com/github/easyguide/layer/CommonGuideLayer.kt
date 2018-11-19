package com.github.easyguide.layer

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.view.View
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

open class CommonGuideLayer(protected val activity: Activity) : AbsGuideLayer() {
    private lateinit var viewContainer: GuideLayerView
    private val targetCache = mutableListOf<Rect>()
    private val viewCache = mutableListOf<View>()
    var onTargetClickListener: OnTargetClickListener? = defaultTargetClick

    protected open fun onViewCreated(context: Context) {
    }

    fun addTargetView(view: View): CommonGuideLayer {
        view.post {
            addTargetView(getViewAbsRect(activity, view))
        }
        return this
    }

    fun addTargetView(rect: Rect): CommonGuideLayer {
        if (this::viewContainer.isInitialized) {
            viewContainer.addTargetsRect(rect)
            viewContainer.requestLayout()
        } else {
            targetCache.add(rect)
        }
        return this
    }

    fun addExtraView(view: View, location: Location, tartgetIndex: Int): CommonGuideLayer {
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
            drawCallBack = this@CommonGuideLayer::onDraw
            targetClickListener = this@CommonGuideLayer::onTargetClick
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

    private fun onTargetClick(id: Int){
        val type = if (id == View.NO_ID) ClickType.OUTSIDE_TARGET else ClickType.ON_TARGET
        onTargetClickListener?.onClick(type, controller)
    }

    protected open fun onDraw(id: Int, rect: Rect, canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    interface OnTargetClickListener {
        fun onClick(type: ClickType, controller: ILayerController)
    }

    enum class ClickType {
        ON_TARGET, OUTSIDE_TARGET;
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

        private val defaultTargetClick = object : OnTargetClickListener {
            override fun onClick(type: ClickType, controller: ILayerController) {
                controller.goNext()
            }
        }
    }
}
