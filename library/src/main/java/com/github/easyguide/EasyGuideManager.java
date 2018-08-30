package com.github.easyguide;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;

import com.github.easyguide.client.CommonGuideClient;
import com.github.easyguide.client.DialogGuideDecorator;
import com.github.easyguide.client.IGuideAction;
import com.github.easyguide.layer.AbsGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements IGuideAction {
    private Context mContext;
    private FrameLayout mParentView;
    private AbsGuideLayer mGuideLayer;
    private IGuideAction mGuideClient;
    
    public static EasyGuideManager create(AbsGuideLayer guideLayer) {
        if (guideLayer == null) {
            throw new IllegalArgumentException("GuideLayer is null!");
        }
        return new EasyGuideManager(guideLayer);
    }

    private EasyGuideManager(AbsGuideLayer guideLayer) {
        this.mGuideLayer = guideLayer;
    }

    public EasyGuideManager with(FrameLayout parentView) {
        this.mContext = parentView.getContext();
        this.mParentView = parentView;
        this.mGuideClient = new CommonGuideClient(this);
        return this;
    }
    
    public EasyGuideManager with(Activity activity) {
        this.mContext = activity;
        this.mParentView = (FrameLayout) activity.getWindow().getDecorView();
        this.mGuideClient = new CommonGuideClient(this);
        return this;
    }

    public EasyGuideManager with(Dialog dialog) {
        this.mContext = dialog.getContext();
        this.mParentView = new FrameLayout(mContext);
        this.mGuideClient = new DialogGuideDecorator(this, new CommonGuideClient(this), dialog);
        return this;
    }

    public Context getContext() {
        return mContext;
    }

    public FrameLayout getParentView() {
        return mParentView;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    @Override
    public void showLayer() {
        if (mParentView == null || mContext == null) {
            throw new IllegalArgumentException("Context or parentView is null! Did you call the method with()?");
        }
        mGuideLayer.setCallback(this);
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
