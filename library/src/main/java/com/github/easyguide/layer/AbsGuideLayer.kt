package com.github.easyguide.layer

import android.content.Context
import android.view.View
import com.github.easyguide.client.ILayerController

/**
 * Created by shenxl on 2018/8/16.
 */

abstract class AbsGuideLayer {
    lateinit var controller: ILayerController
    internal var head: AbsGuideLayer? = null
    internal var next: AbsGuideLayer? = null
    private lateinit var view: View
    var onDismissListener: OnLayerDismissListener? = null
    var onShowListener: OnLayerShowListener? = null

    internal open fun onDismiss() {
        onDismissListener?.onDismiss()
    }

    internal open fun onShow() {
        onShowListener?.onShow()
    }

    internal fun getView(context: Context): View {
        view = if (this::view.isInitialized) view else makeView(context)
        return view
    }

    internal abstract fun makeView(context: Context): View

    interface OnLayerDismissListener {
        /**
         * Callback method to be invoked when the current layer dismisses
         */
        fun onDismiss()
    }

    interface OnLayerShowListener {
        /**
         * Callback method to be invoked when the current layer shows
         */
        fun onShow()
    }
}
