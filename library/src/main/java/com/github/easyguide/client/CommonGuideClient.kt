package com.github.easyguide.client

import android.content.Context
import android.widget.FrameLayout
import com.github.easyguide.layer.AbsGuideLayer

/**
 * Created by shenxl on 2018/8/29.
 */
internal class CommonGuideClient : IGuideClient {
    override lateinit var currentLayer: AbsGuideLayer
    override lateinit var parentView: FrameLayout
    private val context: Context
        get() = parentView.context

    override fun show() {
        parentView.post {
            parentView.addView(currentLayer.getView(context),
                    FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            currentLayer.onShow()
        }
    }

    override fun dismissCurrent() {
        currentLayer.onDismiss()
        val preView = currentLayer.getView(context)
        currentLayer.next?.let {
            currentLayer = it
            this.show()
        }
        parentView.removeView(preView)
    }

    override fun dismissAll() {
        currentLayer.onDismiss()
        parentView.removeView(currentLayer.getView(context))
    }
}
