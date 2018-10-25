package com.github.easyguide.layer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.easyguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class GuideLayerView extends RelativeLayout {
    private static final int DEFAULT_BASE_COLOR = 0x60000000;
    private int mBaseColor = DEFAULT_BASE_COLOR;
    private List<Rect> mTargetRects = new ArrayList<>();
    private List<View> mExtraViews = new ArrayList<>();
    private Paint mPaint;
    private PorterDuffXfermode mXfermode;
    private boolean mHasMarginReset;
    private DrawCallBack mDrawCallBack;
    private LayerClickListener mLayerClickListener;
    private float mDownX, mDownY;

    public GuideLayerView(Context context) {
        super(context);
        init();
    }

    public GuideLayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuideLayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GuideLayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
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

    void addTargetsRect(Rect rect) {
        mTargetRects.add(rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            Location location = (Location) child.getTag();
            if (location.index >= mTargetRects.size()){
                continue;
            }
            Rect targetRect = mTargetRects.get(location.index);
            int verticalAxis = (targetRect.left + targetRect.right) / 2;
            int horizontalAxis = (targetRect.top + targetRect.bottom) / 2;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            switch (location) {
                case TO_TOP:
                    child.layout(verticalAxis - width / 2, targetRect.top - height, verticalAxis + width / 2, targetRect.top);
                    break;
                case TO_BOTTOM:
                    child.layout(verticalAxis - width / 2, targetRect.bottom, verticalAxis + width / 2, targetRect.bottom + height);
                    break;
                case TO_LEFT:
                    child.layout(targetRect.left - width, horizontalAxis - height / 2, targetRect.left, horizontalAxis + height / 2);
                    break;
                case TO_RIGHT:
                    child.layout(targetRect.right, horizontalAxis - height / 2, targetRect.right + width, horizontalAxis + height / 2);
                    break;
                default:
                    child.layout(targetRect.left, targetRect.top, targetRect.right, targetRect.bottom);
                    break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(mBaseColor);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(mXfermode);
        if (mDrawCallBack != null) {
            for (int i = 0; i < mTargetRects.size(); i++) {
                mDrawCallBack.onDraw(i, mTargetRects.get(i), canvas, mPaint);
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
                            if (contains(mTargetRects.get(i), upX, upY)) {
                                mLayerClickListener.onSingleClick(i);
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

    interface DrawCallBack {
        void onDraw(int id, Rect rect, Canvas canvas, Paint paint);
    }

    interface LayerClickListener {
        void onFullClick();

        void onSingleClick(int id);
    }
}
