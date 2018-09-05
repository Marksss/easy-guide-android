package com.github.easyguide.client;

import com.github.easyguide.layer.AbsGuideLayer;

/**
 * Created by shenxl on 2018/9/3.
 */
public interface ILayerChain {
    AbsGuideLayer getCurrentLayer();
    boolean hasNextLayer();
    void stepNext();
}
