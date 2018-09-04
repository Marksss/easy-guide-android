package com.github.easyguide.client;

import android.content.Context;
import android.widget.FrameLayout;

import com.github.easyguide.layer.ILayerCallback;

/**
 * Created by shenxl on 2018/9/4.
 */
public abstract class AbsGuideClient implements ILayerCallback {
    protected ILayerChain mLayerChain;
    protected FrameLayout mParentView;
    protected Context mContext;

    public void setLayerChain(ILayerChain layerChain) {
        mLayerChain = layerChain;
    }

    public void setParentView(FrameLayout parentView) {
        mParentView = parentView;
        mContext = parentView.getContext();
    }

    public abstract void show();
}
