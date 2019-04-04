package com.github.easyguide.client

import android.widget.FrameLayout
import com.github.easyguide.layer.IGuideLayer

/**
 * Created by shenxl on 2018/8/29.
 */
internal class GuideClientImpl : IGuideClient {
    override lateinit var currentLayer: IGuideLayer
    private val parentView: FrameLayout
        get() = currentLayer.parentView
    private val context
        get() = parentView.context

    override fun show() {
        parentView.post {
            currentLayer.onShow()
            currentLayer.getView(context).apply {
                post {
                    requestLayout()
                    parentView.addView(this, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                }
            }
        }
    }

    override fun goNext() {
        currentLayer.onDismiss()
        val preContainer = parentView
        val preView = currentLayer.getView(context)
        currentLayer.next?.let {
            currentLayer = it
            this.show()
        }
        preContainer.removeView(preView)
    }

    override fun dismiss() {
        currentLayer.onDismiss()
        parentView.removeView(currentLayer.getView(context))
    }
}
