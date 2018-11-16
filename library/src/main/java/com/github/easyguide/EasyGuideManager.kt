package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout

import com.github.easyguide.client.IGuideClient
import com.github.easyguide.client.CommonGuideClient
import com.github.easyguide.client.DialogGuideClient
import com.github.easyguide.layer.AbsGuideLayer
import com.github.easyguide.client.ILayerCallback

/**
 * Created by shenxl on 2018/8/16.
 */
class EasyGuideManager private constructor(
        parentView: FrameLayout,
        private val guideClient: IGuideClient) : ILayerCallback by guideClient {
    private var layerAdded: AbsGuideLayer? = null

    init {
        guideClient.parentView = parentView
    }

    constructor(parentView: FrameLayout): this(parentView, CommonGuideClient())
    constructor(activity: Activity): this(activity.window.decorView as FrameLayout, CommonGuideClient())
    constructor(dialog: Dialog): this(FrameLayout(dialog.context), DialogGuideClient(dialog))

    fun addLayer(layer: AbsGuideLayer): EasyGuideManager {
        layer.callback = guideClient
        layerAdded?.let {
            it.next = layer
            layer.head = it.head
        } ?: run {
            layer.head = layer
        }
        layerAdded = layer
        return this
    }

    fun show() {
        guideClient.currentLayer = layerAdded?.head ?:
                throw IllegalArgumentException("Please check if GuideLayers is empty!!!")
        guideClient.show()
    }
}
