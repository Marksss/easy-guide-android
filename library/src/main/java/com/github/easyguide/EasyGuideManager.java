package com.github.easyguide;

import android.app.Activity;
import android.app.Dialog;
import android.widget.FrameLayout;

import com.github.easyguide.client.AbsGuideClient;
import com.github.easyguide.client.CommonGuideClient;
import com.github.easyguide.client.DialogGuideDecoration;
import com.github.easyguide.client.ILayerChain;
import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.layer.ILayerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements ILayerCallback, ILayerChain {
    private List<AbsGuideLayer> mGuideLayers = new ArrayList<>();
    private int mLayerIndex;
    private FrameLayout mParentView;
    private AbsGuideClient mGuideClient;

    private EasyGuideManager(FrameLayout parentView, AbsGuideClient guideClient) {
        mParentView = parentView;
        mGuideClient = guideClient;
        mGuideClient.setParentView(parentView);
        mGuideClient.setLayerChain(this);
    }

    public static EasyGuideManager with(FrameLayout parentView) {
        return new EasyGuideManager(parentView, new CommonGuideClient());
    }

    public static EasyGuideManager with(Activity activity) {
        return new EasyGuideManager(
                (FrameLayout) activity.getWindow().getDecorView(),
                new CommonGuideClient());
    }

    public static EasyGuideManager with(Dialog dialog) {
        return new EasyGuideManager(
                new FrameLayout(dialog.getContext()),
                new DialogGuideDecoration(dialog));
    }

    public EasyGuideManager addLayer(AbsGuideLayer layer) {
        layer.setCallback(this);
        mGuideLayers.add(layer);
        return this;
    }

    public FrameLayout getParentView() {
        return mParentView;
    }

    @Override
    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayers.get(mLayerIndex);
    }

    @Override
    public boolean hasNextLayer() {
        return mLayerIndex < mGuideLayers.size() - 1;
    }

    @Override
    public void stepNext() {
        mLayerIndex++;
    }

    public void show() {
        if (mGuideLayers.isEmpty()) {
            throw new IllegalArgumentException("GuideLayers must not be empty");
        }
        if (mParentView == null) {
            throw new IllegalArgumentException("ParentView is null! Did you call the method with()?");
        }
        mLayerIndex = 0;
        mGuideClient.show();
    }

    @Override
    public void dismissCurrent() {
        mGuideClient.dismissCurrent();
    }

    @Override
    public void dismissAll() {
        mGuideClient.dismissAll();
    }
}
