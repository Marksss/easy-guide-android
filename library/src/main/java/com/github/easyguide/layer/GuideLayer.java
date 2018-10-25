package com.github.easyguide.layer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class GuideLayer extends AbsGuideLayer
        implements GuideLayerView.DrawCallBack, GuideLayerView.LayerClickListener{
    private Activity mActivity;
    private GuideLayerView mViewContainer;
    private List<Rect> mTargetCache = new ArrayList<>();
    private List<View> mViewCache = new ArrayList<>();
    private onFullClickListener mFullClickListener;
    private onSingleClickListener mSingleClickListener;

    public GuideLayer(Activity activity) {
        mActivity = activity;
    }

    protected void onViewCreated(Context context){

    }

    protected Activity getActivity() {
        return mActivity;
    }

    public GuideLayerView getViewContainer() {
        return mViewContainer;
    }

    public final GuideLayer addTargetView(int id){
        return addTargetView(mActivity.findViewById(id));
    }

    public final GuideLayer addTargetView(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                addTargetView(ViewLocUtils.getViewAbsRect(mActivity, view));
            }
        });
        return this;
    }

    public final GuideLayer addTargetView(Rect rect) {
        if (mViewContainer != null) {
            mViewContainer.addTargetsRect(rect);
            mViewContainer.requestLayout();
        } else {
            mTargetCache.add(rect);
        }
        return this;
    }

    public final GuideLayer addExtraView(View view, Location location, int tartgetIndex) {
        location.setIndex(tartgetIndex);
        view.setTag(location);
        if (mViewContainer != null) {
            mViewContainer.addView(view);
        } else {
            mViewCache.add(view);
        }
        return this;
    }

    public void setFullClickListener(onFullClickListener fullClickListener) {
        mFullClickListener = fullClickListener;
    }

    public void setSingleClickListener(onSingleClickListener singleClickListener) {
        mSingleClickListener = singleClickListener;
    }

    @Override
    public final View makeView(Context context) {
        mViewContainer = new GuideLayerView(context);

        mViewContainer.setDrawCallBack(this);
        mViewContainer.setLayerClickListener(this);
        for (int i = 0; i < mTargetCache.size(); i++) {
            addTargetView(mTargetCache.get(i));
        }
        for (int i = 0; i < mViewCache.size(); i++) {
            mViewContainer.addView(mViewCache.get(i));
        }
        onViewCreated(context);

        return mViewContainer;
    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }

    @Override
    public final void onFullClick() {
        if (mSingleClickListener == null) {
            if (mFullClickListener == null) {
                getCallback().dismissCurrent();
            } else {
                mFullClickListener.onClick(mViewContainer, getCallback());
            }
        }
    }

    @Override
    public final void onSingleClick(int id) {
        if (mSingleClickListener != null) {
            mSingleClickListener.onClick(id, mViewContainer, getCallback());
        }
    }

    public interface onFullClickListener{
        void onClick(GuideLayerView container, ILayerCallback callback);
    }

    public interface onSingleClickListener{
        void onClick(int id, GuideLayerView container, ILayerCallback callback);
    }
}
