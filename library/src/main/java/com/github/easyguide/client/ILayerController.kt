package com.github.easyguide.client

/**
 * Created by shenxl on 2018/8/30.
 */
interface ILayerController {
    /**
     * Dismiss the current layer and show the next layer if it exists
     */
    fun goNext()
    /**
     * Dismiss the all layers
     */
    fun dismiss()
}
