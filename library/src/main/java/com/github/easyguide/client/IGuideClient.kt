package com.github.easyguide.client

import android.content.Context
import android.widget.FrameLayout

import com.github.easyguide.layer.ILayerCallback

/**
 * Created by shenxl on 2018/9/4.
 */
interface IGuideClient : ILayerCallback {
    var layerChain: ILayerChain
    var parentView: FrameLayout

    fun show()
}
