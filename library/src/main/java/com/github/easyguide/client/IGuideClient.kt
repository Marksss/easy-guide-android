package com.github.easyguide.client

import android.widget.FrameLayout

/**
 * Created by shenxl on 2018/9/4.
 */
internal interface IGuideClient : ILayerCallback {
    var layerChain: ILayerChain
    var parentView: FrameLayout

    fun show()
}
