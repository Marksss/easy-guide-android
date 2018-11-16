package com.github.easyguide.layer

import android.content.Context
import android.view.View
import com.github.easyguide.client.ILayerCallback

/**
 * Created by shenxl on 2018/8/16.
 */

abstract class AbsGuideLayer {
    internal lateinit var callback: ILayerCallback
    internal var head: AbsGuideLayer? = null
    internal var next: AbsGuideLayer? = null
    private lateinit var layerView: View
    private var onDismissListener: OnLayerDismissListener? = null
    private var onShowListener: OnLayerShowListener? = null

    fun setOnDismissListener(layerDismissListener: OnLayerDismissListener) {
        this.onDismissListener = layerDismissListener
    }

    fun setOnShowListener(layerShowListener: OnLayerShowListener) {
        this.onShowListener = layerShowListener
    }

    fun onDismiss() {
        onDismissListener?.onDismiss()
    }

    fun onShow() {
        onShowListener?.onShow()
    }

    fun getView(context: Context): View {
        layerView = if (this::layerView.isInitialized) layerView else makeView(context)
        return layerView
    }

    protected abstract fun makeView(context: Context): View

    interface OnLayerDismissListener {
        fun onDismiss()
    }

    interface OnLayerShowListener {
        fun onShow()
    }
}
