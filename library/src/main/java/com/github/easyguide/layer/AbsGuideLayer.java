package com.github.easyguide.layer;

import android.content.Context;
import android.view.View;

import com.github.easyguide.layer.ILayerCallback;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class AbsGuideLayer {
    private ILayerCallback mCallback;
    private View mView;
    private onDismissListener mDismissListener;
    private onShowListener mShowListener;

    public final ILayerCallback getCallback() {
        return mCallback;
    }

    public final void setCallback(ILayerCallback callback) {
        mCallback = callback;
    }

    public void setOnDismissListener(onDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public void setOnShowListener(onShowListener showListener) {
        mShowListener = showListener;
    }

    public void onDismiss(){
        if (mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public void onShow(){
        if (mShowListener != null) {
            mShowListener.onShow();
        }
    }

    public final View getView(Context context){
        if (mView == null) {
            mView = makeView(context);
        }
        return mView;
    }

    protected abstract View makeView(Context context);

    public interface onDismissListener{
        void onDismiss();
    }

    public interface onShowListener{
        void onShow();
    }
}
