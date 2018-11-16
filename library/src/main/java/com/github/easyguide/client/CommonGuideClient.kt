package com.github.easyguide.client

import android.content.Context
import android.widget.FrameLayout

/**
 * Created by shenxl on 2018/8/29.
 */
class CommonGuideClient : IGuideClient {
    override lateinit var layerChain: ILayerChain
    override lateinit var parentView: FrameLayout
    private val context:Context by lazy { parentView.context }

    override fun show() {
        parentView.post {
            parentView.addView(layerChain.currentLayer.getView(context),
                    FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            layerChain.currentLayer.onShow()
        }
    }

    override fun dismissCurrent() {
        layerChain.currentLayer.onDismiss()
        val preView = layerChain.currentLayer.getView(context)
        if (layerChain.hasNextLayer()) {
            layerChain.stepNext()
            this.show()
        }
        parentView.removeView(preView)
    }

    override fun dismissAll() {
        layerChain.currentLayer.onDismiss()
        parentView.removeView(layerChain.currentLayer.getView(context))
    }
}
