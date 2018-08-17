package com.github.easyguide;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeLayerView extends RelativeLayout {
    public static final int DEFAULT_BACKGROUND_COLOR = 0xb3000000;
    private static final Object FLAG_MASK = new Object();
    public List<MaskEntity> mMaskEntities = new ArrayList<>();
    private Paint mPaint;

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

    public void setMaskEntities(List<MaskEntity> maskEntities) {
        if (maskEntities != null) {
            mMaskEntities.addAll(maskEntities);
            for (MaskEntity entity : mMaskEntities) {
                View view = new View(getContext());
                Rect rect = entity.mRect;
                LayoutParams params = new LayoutParams((int) (rect.right - rect.left), (int) (rect.bottom - rect.top));
                params.setMargins((int) rect.left, (int) rect.top, 0, 0);
                view.setLayoutParams(params);
                view.setId(entity.id);
                addView(view);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);
        for (MaskEntity entity : mMaskEntities) {
            if (entity.needHighLight) {
                canvas.drawRect(entity.mRect, mPaint);
            }
        }

        super.dispatchDraw(canvas);
    }
}
