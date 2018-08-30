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
    Context mContext;
    FrameLayout mParentView;
    AbsGuideLayer mGuideLayer;

    public CommonGuideClient(EasyGuideManager manager) {
        mContext = manager.getContext();
        mParentView = manager.getParentView();
        mGuideLayer = manager.getCurrentLayer();
    }

    @Override
    public void showLayer() {
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
