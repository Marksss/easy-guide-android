package com.github.easyguide.client;

import com.github.easyguide.layer.AbsGuideLayer;

/**
 * Created by shenxl on 2018/8/29.
 */
public interface IGuideAction extends AbsGuideLayer.ILayerCallback {
    void showLayer();
}
