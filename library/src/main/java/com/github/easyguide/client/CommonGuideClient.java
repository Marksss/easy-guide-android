package com.github.easyguide.client;

import android.view.View;
import android.widget.FrameLayout;

import com.github.easyguide.EasyGuideManager;

/**
 * Created by shenxl on 2018/8/29.
 */
public class CommonGuideClient extends AbsGuideClient {

    @Override
    public void show() {
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(mLayerChain.getCurrentLayer().getView(mContext),
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                mLayerChain.getCurrentLayer().onShow();
            }
        });
    }

    @Override
    public void dismissCurrent() {
        mLayerChain.getCurrentLayer().onDismiss();
        View preView = mLayerChain.getCurrentLayer().getView(mContext);
        if (mLayerChain.hasNextLayer()) {
            mLayerChain.stepNext();
            this.show();
        }
        mParentView.removeView(preView);
    }

    @Override
    public void dismissAll() {
        mLayerChain.getCurrentLayer().onDismiss();
        mParentView.removeView(mLayerChain.getCurrentLayer().getView(mContext));
    }
}
