package com.github.easyguide.client;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.github.easyguide.EasyGuideManager;

/**
 * Created by shenxl on 2018/8/30.
 */
public class DialogGuideDecoration extends AbsGuideClient {
    PopupWindow mPopupWindow;
    View mDecorView;
    AbsGuideClient mClient;

    public DialogGuideDecoration(Dialog dialog) {
        if (dialog.getWindow() != null) {
            mDecorView = dialog.getWindow().getDecorView();
        }
        mClient = new CommonGuideClient();
    }

    @Override
    public void setLayerChain(ILayerChain layerChain) {
        super.setLayerChain(layerChain);
        mClient.setLayerChain(layerChain);
    }

    @Override
    public void setParentView(FrameLayout parentView) {
        super.setParentView(parentView);
        mClient.setParentView(parentView);
    }

    @Override
    public void show() {
        if (mDecorView != null) {
            mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(mParentView);
            mPopupWindow.showAsDropDown(mDecorView);
        }
        mClient.show();
    }

    @Override
    public void dismissCurrent() {
        if (!mLayerChain.hasNextLayer() && mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mClient.dismissCurrent();
    }

    @Override
    public void dismissAll() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mClient.dismissAll();
    }
}
