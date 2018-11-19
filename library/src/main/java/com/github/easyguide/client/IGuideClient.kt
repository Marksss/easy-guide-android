package com.github.easyguide.client

import android.widget.FrameLayout
import com.github.easyguide.layer.AbsGuideLayer

/**
 * Created by shenxl on 2018/9/4.
 */
internal interface IGuideClient : ILayerController {
    var currentLayer: AbsGuideLayer
    var parentView: FrameLayout

    fun show()
}
