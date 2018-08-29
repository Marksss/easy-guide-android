package com.github.easyguide;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements AbsGuideLayer.ILayerCallback {
    private Context mContext;
    private FrameLayout mParentView;
    private AbsGuideLayer mGuideLayer;
    
    public static EasyGuideManager create(AbsGuideLayer guideLayer) {
        if (guideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }
        return new EasyGuideManager(guideLayer);
    }

    private EasyGuideManager(AbsGuideLayer guideLayer) {
        this.mGuideLayer = guideLayer;
    }
    
    public EasyGuideManager with(Activity activity) {
        this.mContext = activity;
        if (mParentView == null) {
            mParentView = (FrameLayout) activity.getWindow().getDecorView();
        }
        return this;
    }

    public EasyGuideManager with(Dialog dialog) {
        this.mContext = dialog.getContext();
        if (mParentView == null) {
            mParentView = new FrameLayout(mContext);
        }
        if (dialog.getWindow() != null) {
            PopupWindow mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(mParentView);
            mPopupWindow.showAsDropDown(dialog.getWindow().getDecorView());
        }
        return this;
    }

    public EasyGuideManager setParentView(FrameLayout parentView) {
        this.mParentView = parentView;
        return this;
    }

    public FrameLayout getParentView() {
        return mParentView;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    public void showLayer() {
        mGuideLayer.setCallback(this);
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(mGuideLayer.getView(mContext),
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }

    @Override
    public void dismissCurrent() {
        if (mParentView != null) {
            View preView = mGuideLayer.getView(mContext);
            AbsGuideLayer nextLayer = mGuideLayer.nextLayer();
            if (nextLayer != null) {
                mGuideLayer = nextLayer;
                showLayer();
            }
            mParentView.removeView(preView);
        }
    }

    @Override
    public void dismissAll() {
        if (mParentView != null) {
            mParentView.removeView(mGuideLayer.getView(mContext));
        }
    }
}
