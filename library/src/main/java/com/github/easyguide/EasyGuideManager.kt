package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout

import com.github.easyguide.client.IGuideClient
import com.github.easyguide.client.CommonGuideClient
import com.github.easyguide.client.DialogGuideClient
import com.github.easyguide.layer.AbsGuideLayer
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */
class EasyGuideManager private constructor(
        parentView: FrameLayout,
        private val guideClient: IGuideClient) : ILayerController by guideClient {

    private var layerAdded: AbsGuideLayer? = null
    var currentLayer: AbsGuideLayer? = null
        private set
        get() = guideClient.currentLayer

    init {
        guideClient.parentView = parentView
    }

    /**
     * Just add layers to the FrameLayout
     * @param parentView
     */
    constructor(parentView: FrameLayout) : this(parentView, CommonGuideClient())

    /**
     * Add layers to decorView
     * @param activity
     */
    constructor(activity: Activity) : this(activity.window.decorView as FrameLayout, CommonGuideClient())

    /**
     * Show layers on a dialog
     * @param dialog
     */
    constructor(dialog: Dialog) : this(FrameLayout(dialog.context), DialogGuideClient(dialog))

    /**
     * Call before {@link #show()}
     * @param layer
     */
    fun addLayer(layer: AbsGuideLayer): EasyGuideManager {
        layer.controller = guideClient

        // chain-of-responsibility pattern
        layerAdded?.let {
            it.next = layer
            layer.head = it.head
        } ?: run {
            layer.head = layer
        }
        layerAdded = layer
        return this
    }

    /**
     * Show the first layer that is added
     */
    fun show() {
        guideClient.currentLayer = layerAdded?.head ?: throw IllegalArgumentException("Please check if GuideLayers is empty!!!")
        guideClient.show()
    }
}
