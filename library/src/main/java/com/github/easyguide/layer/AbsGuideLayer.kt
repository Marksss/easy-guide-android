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
    var onDismissListener: OnLayerDismissListener? = null
    var onShowListener: OnLayerShowListener? = null

    internal fun onDismiss() {
        onDismissListener?.onDismiss()
    }

    internal fun onShow() {
        onShowListener?.onShow()
    }

    internal fun getView(context: Context): View {
        layerView = if (this::layerView.isInitialized) layerView else makeView(context)
        return layerView
    }

    internal abstract fun makeView(context: Context): View

    interface OnLayerDismissListener {
        fun onDismiss()
    }

    interface OnLayerShowListener {
        fun onShow()
    }
}
