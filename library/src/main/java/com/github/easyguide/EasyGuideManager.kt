package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout

import com.github.easyguide.client.IGuideClient
import com.github.easyguide.client.CommonGuideClient
import com.github.easyguide.client.DialogGuideClient
import com.github.easyguide.client.ILayerChain
import com.github.easyguide.layer.AbsGuideLayer
import com.github.easyguide.layer.ILayerCallback

/**
 * Created by shenxl on 2018/8/16.
 */
class EasyGuideManager private constructor(
        parentView: FrameLayout,
        private val mGuideClient: IGuideClient) : ILayerCallback by mGuideClient, ILayerChain {

    init {
        mGuideClient.parentView = parentView
        mGuideClient.layerChain = this
    }

    constructor(parentView: FrameLayout): this(parentView, CommonGuideClient())
    constructor(activity: Activity): this(activity.window.decorView as FrameLayout, CommonGuideClient())
    constructor(dialog: Dialog): this(FrameLayout(dialog.context), DialogGuideClient(dialog))

    private val mGuideLayers = mutableListOf<AbsGuideLayer>()
    private lateinit var layerIterator: MutableListIterator<AbsGuideLayer>
    override lateinit var currentLayer: AbsGuideLayer
        private set

    fun addLayer(layer: AbsGuideLayer): EasyGuideManager {
        layer.callback = this
        mGuideLayers.add(layer)
        return this
    }

    override fun hasNextLayer(): Boolean {
        return layerIterator.hasNext()
    }

    override fun stepNext() {
        currentLayer = layerIterator.next()
    }

    fun show() {
        if (mGuideLayers.isEmpty()) {
            throw IllegalArgumentException("Please check if GuideLayers is empty!!!")
        }
        layerIterator = mGuideLayers.listIterator()
        stepNext()
        mGuideClient.show()
    }
}
