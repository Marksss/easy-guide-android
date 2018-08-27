package com.github.easyguide;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideWindow extends DialogFragment implements AbsGuideLayer.ILayerCallback {
    private FrameLayout mParentView;
    private Context mContext;
    private AbsGuideLayer mGuideLayer;

    public EasyGuideWindow(Context context, AbsGuideLayer guideLayer) {
        this.mContext = context;
        this.mGuideLayer = guideLayer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mParentView = new FrameLayout(mContext);
        mParentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return mParentView;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    private void show() {
        if (mGuideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }

        mGuideLayer.setCallback(this);
        View view = mGuideLayer.makeView(mContext);
        mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog == null ? null : dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        show();
    }

    @Override
    public void dismissCurrent() {
        AbsGuideLayer nextLayer = mGuideLayer.nextLayer();
        if (nextLayer == null) {
            dismiss();
        } else {
            mGuideLayer = nextLayer;
            mParentView.removeAllViews();
            show();
        }
    }

    @Override
    public void dismissAll() {
        dismiss();
    }
}
