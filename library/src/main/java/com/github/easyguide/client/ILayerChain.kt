package com.github.easyguide.client

import com.github.easyguide.layer.AbsGuideLayer

/**
 * Created by shenxl on 2018/9/3.
 */
interface ILayerChain {
    val currentLayer: AbsGuideLayer
    fun hasNextLayer(): Boolean
    fun stepNext()
}
