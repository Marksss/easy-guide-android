package com.github.easyguide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeGuideLayer extends AbsGuideLayer
        implements RelativeLayerView.DrawCallBack, RelativeLayerView.WindowCircleLinster,
        RelativeLayerView.LayerClickListener{
    private Activity mActivity;
    private RelativeLayerView mViewContainer;
    private SparseArray<Rect> mTargetCache = new SparseArray<>();
    private onFullClickListener mFullClickListener;
    private onSingleClickListener mSingleClickListener;
    private AbsGuideLayer mNextLayer;

    public RelativeGuideLayer(Activity activity) {
        mActivity = activity;
    }

    protected RelativeLayerView onCreateView(Context context){
        return null;
    }

    protected Activity getActivity() {
        return mActivity;
    }

    public RelativeLayerView getViewContainer() {
        return mViewContainer;
    }

    public RelativeGuideLayer addTargetView(int id){
        return addTargetView(id, ViewLocationUtils.getViewAbsRect(mActivity, mActivity.findViewById(id)));
    }

    public RelativeGuideLayer addTargetView(View view){
        return addTargetView(view.getId(), ViewLocationUtils.getViewAbsRect(mActivity, view));
    }

    public RelativeGuideLayer addTargetView(int id, Rect rect) {
        if (mViewContainer != null) {
            mViewContainer.addTargetsRect(id, rect);
        } else {
            mTargetCache.put(id, rect);
        }
        return this;
    }

    public void setFullClickListener(onFullClickListener fullClickListener) {
        mFullClickListener = fullClickListener;
    }

    public void setSingleClickListener(onSingleClickListener singleClickListener) {
        mSingleClickListener = singleClickListener;
    }

    public void setNextLayer(AbsGuideLayer nextLayer) {
        mNextLayer = nextLayer;
    }

    @Override
    public final View makeView(Context context) {
        RelativeLayerView view = onCreateView(context);
        if (view == null) {
            view = new RelativeLayerView(context);
        }
        mViewContainer = view;

        view.setDrawCallBack(this);
        view.setWindowCircleLinster(this);
        view.setLayerClickListener(this);
        for (int i = 0; i < mTargetCache.size(); i++) {
            addTargetView(mTargetCache.keyAt(i), mTargetCache.valueAt(i));
        }

        return view;
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return mNextLayer;
    }

    @Override
    public void onLayerAttached(RelativeLayerView view) {

    }

    @Override
    public void onLayerDetached(RelativeLayerView view) {

    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint){
        drawRect(rect, canvas, paint);
    }

    protected void drawRect(Rect rect, Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }

    @Override
    public void onFullClick() {
        if (mSingleClickListener == null) {
            if (mFullClickListener == null) {
                getCallback().dismissCurrent();
            } else {
                mFullClickListener.onClick(mViewContainer, getCallback());
            }
        }
    }

    @Override
    public void onSingleClick(int id) {
        if (mSingleClickListener != null) {
            mSingleClickListener.onClick(id, mViewContainer, getCallback());
        }
    }

    public interface onFullClickListener{
        void onClick(RelativeLayerView container, ILayerCallback callback);
    }

    public interface onSingleClickListener{
        void onClick(int id, RelativeLayerView container, ILayerCallback callback);
    }
}
