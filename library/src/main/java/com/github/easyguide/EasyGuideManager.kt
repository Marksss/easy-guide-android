package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout
import com.github.easyguide.client.GuideClientImpl
import com.github.easyguide.client.IGuideClient
import com.github.easyguide.client.ILayerController
import com.github.easyguide.layer.IGuideLayer

/**
 * Created by shenxl on 2018/8/16.
 */
class EasyGuideManager {

    private var headLayer: IGuideLayer? = null
    private var tailLayer: IGuideLayer? = null
    private val guideClient: IGuideClient = GuideClientImpl()
    val currentLayer: IGuideLayer?
        get() = guideClient.currentLayer

    /**
     * Call before {@link #show()}
     * @param layer
     */
    fun addLayer(layer: IGuideLayer): EasyGuideManager {
        layer.controller = guideClient
        // chain-of-responsibility pattern
        if (headLayer == null) {
            headLayer = layer
        }
        tailLayer?.next = layer
        tailLayer = layer
        return this
    }

    /**
     * Show the first layer that is added
     */
    fun show() {
        guideClient.currentLayer = headLayer ?: throw IllegalArgumentException("Please check if GuideLayers is empty!!!")
        guideClient.show()
    }
}
