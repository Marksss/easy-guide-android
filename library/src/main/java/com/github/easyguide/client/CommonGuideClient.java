package com.github.easyguide.client;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.AbsGuideLayer;

/**
 * Created by shenxl on 2018/8/29.
 */
public class CommonGuideClient implements IGuideAction {
    EasyGuideManager mManager;
    FrameLayout mParentView;

    public CommonGuideClient(EasyGuideManager manager) {
        this.mManager = manager;
        this.mParentView = manager.getParentView();
    }

    @Override
    public void showLayer() {
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(mManager.getCurrentLayer().getView(mManager.getContext()),
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }

    @Override
    public void dismissCurrent() {
        View preView = mManager.getCurrentLayer().getView(mManager.getContext());
        if (mManager.getNextLayer() != null) {
            mManager.stepNext();
            mManager.showLayer();
        }
        mParentView.removeView(preView);
    }

    @Override
    public void dismissAll() {
        mParentView.removeView(mManager.getCurrentLayer().getView(mManager.getContext()));
    }
}
