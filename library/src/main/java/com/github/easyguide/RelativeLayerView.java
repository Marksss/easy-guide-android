package com.github.easyguide;

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

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeLayerView extends RelativeLayout {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xb3000000;
    private static final Object FLAG_MASK = new Object();
    private SparseArray<Rect> mTargetRects = new SparseArray<>();
    private Paint mPaint;
    private boolean mHasMarginReset;
    private DrawCallBack mDrawCallBack;
    private WindowCircleLinster mWindowCircleLinster;

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

    void setDrawCallBack(DrawCallBack drawCallBack) {
        mDrawCallBack = drawCallBack;
    }

    public void setWindowCircleLinster(WindowCircleLinster windowCircleLinster) {
        mWindowCircleLinster = windowCircleLinster;
    }

    void addTargetsRect(int id, Rect rect) {
        if (id != NO_ID) {
            mTargetRects.put(id, rect);
        } else {
            mTargetRects.put(mTargetRects.size(), rect);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mWindowCircleLinster != null) {
            mWindowCircleLinster.onLayerAttached(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWindowCircleLinster != null) {
            mWindowCircleLinster.onLayerDetached(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mHasMarginReset && mTargetRects.size() > 0) {
            mHasMarginReset = true;
            int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
            int totalHeight = MeasureSpec.getSize(heightMeasureSpec);

            for (int i = 0; i < getChildCount(); i++){
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.mTargetAbove != NO_ID) {
                    layoutParams.bottomMargin += totalHeight - mTargetRects.get(layoutParams.mTargetAbove).top;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
                if (layoutParams.mTargetBelow != NO_ID) {
                    layoutParams.topMargin += mTargetRects.get(layoutParams.mTargetBelow).bottom;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                }
                if (layoutParams.mTargetToLeft != NO_ID) {
                    layoutParams.rightMargin += totalWidth - mTargetRects.get(layoutParams.mTargetToLeft).left;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                if (layoutParams.mTargetToRight != NO_ID) {
                    layoutParams.leftMargin += mTargetRects.get(layoutParams.mTargetToRight).right;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                if (layoutParams.mTargetAlignTop != NO_ID) {
                    layoutParams.topMargin += mTargetRects.get(layoutParams.mTargetAlignTop).top;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                }
                if (layoutParams.mTargetAlignBottom != NO_ID) {
                    layoutParams.bottomMargin += totalHeight - mTargetRects.get(layoutParams.mTargetAlignBottom).bottom;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
                if (layoutParams.mTargetAlignLeft != NO_ID) {
                    layoutParams.leftMargin += mTargetRects.get(layoutParams.mTargetAlignLeft).left;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                if (layoutParams.mTargetAlignRight != NO_ID) {
                    layoutParams.rightMargin += totalWidth - mTargetRects.get(layoutParams.mTargetAlignRight).right;
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                child.setLayoutParams(layoutParams);
            }
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);
        for (int i = 0; i < mTargetRects.size(); i++) {
            mDrawCallBack.onDraw(mTargetRects.keyAt(i), mTargetRects.valueAt(i), canvas, mPaint);
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
        public int mTargetAbove, mTargetBelow, mTargetToLeft, mTargetToRight;
        public int mTargetAlignTop, mTargetAlignBottom, mTargetAlignLeft, mTargetAlignRight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.RelativeLayerView);
            mTargetAbove = ta.getResourceId(R.styleable.RelativeLayerView_target_above, NO_ID);
            mTargetBelow = ta.getResourceId(R.styleable.RelativeLayerView_target_below, NO_ID);
            mTargetToLeft = ta.getResourceId(R.styleable.RelativeLayerView_target_toLeft, NO_ID);
            mTargetToRight = ta.getResourceId(R.styleable.RelativeLayerView_target_toRight, NO_ID);

            mTargetAlignBottom = ta.getResourceId(R.styleable.RelativeLayerView_target_alignBottom, NO_ID);
            mTargetAlignTop = ta.getResourceId(R.styleable.RelativeLayerView_target_alignTop, NO_ID);
            mTargetAlignLeft= ta.getResourceId(R.styleable.RelativeLayerView_target_alignLeft, NO_ID);
            mTargetAlignRight = ta.getResourceId(R.styleable.RelativeLayerView_target_alignRight, NO_ID);
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

    interface DrawCallBack{
        void onDraw(int id, Rect rect, Canvas canvas, Paint paint);
    }

    interface WindowCircleLinster {
        void onLayerAttached(RelativeLayerView view);
        void onLayerDetached(RelativeLayerView view);
    }
}
