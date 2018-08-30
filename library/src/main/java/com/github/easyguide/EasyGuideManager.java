package com.github.easyguide;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;

import com.github.easyguide.client.CommonGuideClient;
import com.github.easyguide.client.DialogGuideClient;
import com.github.easyguide.client.IGuideAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements IGuideAction {
    private List<AbsGuideLayer> mGuideLayers = new ArrayList<>();
    private int mLayerIndex = 0;
    private FrameLayout mParentView;
    private IGuideAction mGuideClient;

    public EasyGuideManager(FrameLayout parentView) {
        mParentView = parentView;
    }

    public static EasyGuideManager with(FrameLayout parentView) {
        EasyGuideManager manager = new EasyGuideManager(parentView);
        manager.setGuideClient(new CommonGuideClient(manager));
        return manager;
    }
    
    public static EasyGuideManager with(Activity activity) {
        EasyGuideManager manager = new EasyGuideManager((FrameLayout) activity.getWindow().getDecorView());
        manager.setGuideClient(new CommonGuideClient(manager));
        return manager;
    }

    public static EasyGuideManager with(Dialog dialog) {
        EasyGuideManager manager = new EasyGuideManager(new FrameLayout(dialog.getContext()));
        manager.setGuideClient(new DialogGuideClient(manager, dialog));
        return manager;
    }

    public EasyGuideManager addLayer(AbsGuideLayer layer){
        mGuideLayers.add(layer);
        return this;
    }

    private void setGuideClient(IGuideAction guideClient) {
        mGuideClient = guideClient;
    }

    public Context getContext() {
        return mParentView.getContext();
    }

    public FrameLayout getParentView() {
        return mParentView;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayers.get(mLayerIndex);
    }

    public boolean hasNextLayer() {
        return mLayerIndex < mGuideLayers.size() - 1;
    }

    public void stepNext() {
        mLayerIndex++;
    }

    @Override
    public void showLayer() {
        if (mGuideLayers.isEmpty()) {
            throw new IllegalArgumentException("GuideLayer must not be null");
        }
        if (mParentView == null) {
            throw new IllegalArgumentException("ParentView is null! Did you call the method with()?");
        }
        getCurrentLayer().setCallback(this);
        mGuideClient.showLayer();
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
