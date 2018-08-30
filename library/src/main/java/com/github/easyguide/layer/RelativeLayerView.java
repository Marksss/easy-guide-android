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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.easyguide.R;

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeLayerView extends RelativeLayout {
    private static final int DEFAULT_BASE_COLOR = 0x60000000;
    private int mBaseColor = DEFAULT_BASE_COLOR;
    private SparseArray<Rect> mTargetRects = new SparseArray<>();
    private Paint mPaint;
    private boolean mHasMarginReset;
    private DrawCallBack mDrawCallBack;
    private LayerClickListener mLayerClickListener;
    private float mDownX, mDownY;

    public RelativeLayerView(Context context) {
        super(context);
        init(null);
    }

    public RelativeLayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RelativeLayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public RelativeLayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RelativeLayerView);
            try {
                mBaseColor = a.getColor(R.styleable.RelativeLayerView_layer_base_color, DEFAULT_BASE_COLOR);
            } finally {
                a.recycle();
            }
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);

        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setWillNotDraw(false);
    }

    public void setBaseColor(int baseColor) {
        mBaseColor = baseColor;
    }

    void setDrawCallBack(DrawCallBack drawCallBack) {
        mDrawCallBack = drawCallBack;
    }

    public void setLayerClickListener(LayerClickListener layerClickListener) {
        mLayerClickListener = layerClickListener;
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
        if (mTargetRects.size() > 0) {
            super.onLayout(changed, l, t, r, b);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(mBaseColor);
        if (mDrawCallBack != null) {
            for (int i = 0; i < mTargetRects.size(); i++) {
                mDrawCallBack.onDraw(mTargetRects.keyAt(i), mTargetRects.valueAt(i), canvas, mPaint);
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float upX = event.getX();
                float upY = event.getY();
                if (Math.abs(upX - mDownX) < 10 && Math.abs(upY - mDownY) < 10) {
                    if (mLayerClickListener != null) {
                        mLayerClickListener.onFullClick();
                        for (int i = 0; i < mTargetRects.size(); i++) {
                            if (contains(mTargetRects.valueAt(i), upX, upY)){
                                mLayerClickListener.onSingleClick(mTargetRects.keyAt(i));
                                return true;
                            }
                        }
                    }
                    performClick();
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    private boolean contains(Rect rect, float x, float y) {
        return rect.left < rect.right && rect.top < rect.bottom  // check for empty first
                && x >= rect.left && x < rect.right && y >= rect.top && y < rect.bottom;
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

    interface LayerClickListener{
        void onFullClick();
        void onSingleClick(int id);
    }
}
