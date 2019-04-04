package com.github.easyguide.layer

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

interface IGuideLayer {
    var controller: ILayerController
    var next: IGuideLayer?
    var parentView: FrameLayout

    fun onDismiss()
    fun onShow()
    fun getView(context: Context): View
}
