package com.github.easyguide.layer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.easyguide.R;
import com.github.easyguide.utils.ViewLocationUtils;

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeLayerView extends RelativeLayout {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xb3000000;
    private static final Object FLAG_MASK = new Object();
    private SparseArray<Rect> mTargetRects = new SparseArray<>();
    private Paint mPaint;
    private boolean mHasMarginReset;

    public RelativeLayerView(Context context) {
        super(context);
        init();
    }

    public RelativeLayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelativeLayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RelativeLayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);

        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setWillNotDraw(false);
    }

    void addTargetsRect(int id, Rect rect) {
        if (id != NO_ID) {
            mTargetRects.put(id, rect);
        } else {
            mTargetRects.put(mTargetRects.size(), rect);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHasMarginReset) {
            return;
        }
        mHasMarginReset = true;

        for (int j = 0; j < mTargetRects.size(); j++) {
            int id = mTargetRects.keyAt(j);
            Rect rect = mTargetRects.valueAt(j);
            for (int i = 0; i < getChildCount(); i++){
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (id == layoutParams.mTargetId){
                    if (layoutParams.mIsBindToTop) {
                        layoutParams.bottomMargin += getMeasuredHeight() - rect.top - ViewLocationUtils.mOffsetY;
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }
                    if (layoutParams.mIsBindToBottom) {
                        layoutParams.topMargin += rect.bottom;
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    }
                    if (layoutParams.mIsBindToLeft) {
                        layoutParams.rightMargin += getMeasuredWidth() - rect.left;
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                    if (layoutParams.mIsBindToRight) {
                        layoutParams.leftMargin += rect.right;
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    }
                    child.setLayoutParams(layoutParams);
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);
        for (int i = 0; i < mTargetRects.size(); i++) {
            canvas.drawRect(mTargetRects.valueAt(i), mPaint);
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public Rect getTargetRect(int targetId){
        if (mTargetRects.indexOfKey(targetId) >= 0){
            return mTargetRects.get(targetId);
        } else {
            return mTargetRects.valueAt(0);
        }
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {
        boolean mIsBindToTop, mIsBindToBottom, mIsBindToLeft, mIsBindToRight;
        int mTargetId;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.RelativeLayerView);
            mIsBindToTop = ta.getBoolean(R.styleable.RelativeLayerView_target_bindToTop, false);
            mIsBindToBottom = ta.getBoolean(R.styleable.RelativeLayerView_target_bindToBottom, false);
            mIsBindToLeft = ta.getBoolean(R.styleable.RelativeLayerView_target_bindToLeft, false);
            mIsBindToRight = ta.getBoolean(R.styleable.RelativeLayerView_target_bindToRight, false);
            mTargetId = ta.getResourceId(R.styleable.RelativeLayerView_target_bindId, NO_ID);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
