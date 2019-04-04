package com.github.easyguide.client

import android.widget.FrameLayout
import com.github.easyguide.layer.IGuideLayer

/**
 * Created by shenxl on 2018/9/4.
 */
internal interface IGuideClient : ILayerController {
    var currentLayer: IGuideLayer

    fun show()
}
