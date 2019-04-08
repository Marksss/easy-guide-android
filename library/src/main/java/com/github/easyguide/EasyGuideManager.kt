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
    val currentLayerIndex: Int
        get() = currentLayer.let{
            var index = 0
            var layer = headLayer
            while (true) {
                if (layer == it) {
                    break
                } else if (layer == null) {
                    index = 0
                    break
                }
                index++
                layer = layer.next
            }
            index
        }

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
     * show layers from the head of the chain
     */
    fun show() {
        show(0)
    }

    /**
     * show layers in the chain
     * @param index the index of layer that is going to be shown first
     */
    fun show(index: Int) {
        var i = index
        var firstLayer = headLayer
        while (true) {
            if (i-- <= 0) {
                break
            }
            firstLayer = firstLayer?.next
        }

        guideClient.currentLayer = firstLayer ?: throw IllegalArgumentException("Please check if GuideLayers is empty or index is wrong")
        guideClient.show()
    }
}
