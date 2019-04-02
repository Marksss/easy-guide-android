package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout
import com.github.easyguide.client.GuideClientImpl
import com.github.easyguide.client.IGuideClient
import com.github.easyguide.client.ILayerController
import com.github.easyguide.layer.AbsGuideLayer

/**
 * Created by shenxl on 2018/8/16.
 */
class EasyGuideManager private constructor(
        parentView: FrameLayout,
        private val guideClient: IGuideClient) : ILayerController by guideClient {

    private var headLayer: AbsGuideLayer? = null
    private var tailLayer: AbsGuideLayer? = null
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
    constructor(parentView: FrameLayout) : this(parentView, GuideClientImpl())

    /**
     * Add layers to decorView
     * @param activity
     */
    constructor(activity: Activity) : this(activity.window.decorView as FrameLayout)

    /**
     * Show layers on a dialog
     * @param dialog
     */
    constructor(dialog: Dialog) : this(dialog.window.decorView as FrameLayout)

    /**
     * Call before {@link #show()}
     * @param layer
     */
    fun addLayer(layer: AbsGuideLayer): EasyGuideManager {
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
