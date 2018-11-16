package com.github.easyguide

import android.app.Activity
import android.app.Dialog
import android.widget.FrameLayout

import com.github.easyguide.client.AbsGuideClient
import com.github.easyguide.client.CommonGuideClient
import com.github.easyguide.client.DialogGuideDecoration
import com.github.easyguide.client.ILayerChain
import com.github.easyguide.layer.AbsGuideLayer
import com.github.easyguide.layer.ILayerCallback

/**
 * Created by shenxl on 2018/8/16.
 */

class EasyGuideManager private constructor(
        parentView: FrameLayout,
        private val mGuideClient: AbsGuideClient) : ILayerCallback by mGuideClient, ILayerChain {

    private val mGuideLayers = mutableListOf<AbsGuideLayer>()
    private lateinit var layerIterator: MutableListIterator<AbsGuideLayer>
    override lateinit var currentLayer: AbsGuideLayer
        private set

    init {
        mGuideClient.setParentView(parentView)
        mGuideClient.setLayerChain(this)
    }

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
            throw IllegalArgumentException("GuideLayers must not be empty")
        }
        currentLayer = mGuideLayers[0]
        layerIterator = mGuideLayers.listIterator()
        mGuideClient.show()
    }

    companion object {

        fun with(parentView: FrameLayout): EasyGuideManager {
            return EasyGuideManager(parentView, CommonGuideClient())
        }

        fun with(activity: Activity): EasyGuideManager {
            return EasyGuideManager(
                    activity.window.decorView as FrameLayout,
                    CommonGuideClient())
        }

        fun with(dialog: Dialog): EasyGuideManager {
            return EasyGuideManager(
                    FrameLayout(dialog.context),
                    DialogGuideDecoration(dialog))
        }
    }
}
