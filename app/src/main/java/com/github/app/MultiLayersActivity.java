package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.ILayerCallback;
import com.github.easyguide.layer.Location;
import com.github.easyguide.layer.GuideLayer;
import com.github.easyguide.layer.GuideLayerView;

/**
 * Created by shenxl on 2018/8/14.
 */
public class MultiLayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_layers);

        /*   EasyGuide starts  */
        MultiLayer0 layer = new MultiLayer0(MultiLayersActivity.this);
        layer.setSingleClickListener(new GuideLayer.onSingleTargetClickListener() {
            @Override
            public void onClick(int id, GuideLayerView container, ILayerCallback callback) {
                callback.dismissCurrent();
            }
        });
        EasyGuideManager.
                with(MultiLayersActivity.this).
                addLayer(layer).
                addLayer(new MultiLayer1(MultiLayersActivity.this)).
                addLayer(new MultiLayer2(MultiLayersActivity.this)).
                show();
        /*   EasyGuide ends  */
    }

    public class MultiLayer0 extends GuideLayer {

        public MultiLayer0(Activity activity) {
            super(activity);
        }

        @Override
        protected void onViewCreated(Context context){
            addTargetView(R.id.multi_guide_0);
            addTargetView(R.id.multi_guide_1);
            addExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_0, null), Location.TO_BOTTOM,0);
            addExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_1, null), Location.TO_TOP,1);
        }

        @Override
        public void onDraw(int id, Rect rect, Canvas canvas, Paint paint) {
            canvas.drawRoundRect(new RectF(rect), 10, 10, paint);
        }
    }

    public class MultiLayer1 extends GuideLayer {
        public MultiLayer1(Activity activity) {
            super(activity);
        }

        @Override
        protected void onViewCreated(Context context){
            addTargetView(R.id.multi_guide_circle);
            addExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_1, null), Location.TO_TOP,0);
        }

        @Override
        public void onDraw(int id, Rect rect, Canvas canvas, Paint paint) {
            float cx = (rect.left + rect.right)/2;
            float cy = (rect.top + rect.bottom)/2;
            float radius = Math.max((rect.right - rect.left)/2, (rect.bottom - rect.top)/2) + 10;
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }

    public class MultiLayer2 extends GuideLayer {
        public MultiLayer2(Activity activity) {
            super(activity);
            addTargetView(R.id.multi_guide_ladder);
        }

        @Override
        public void onDraw(int id, Rect rect, Canvas canvas, Paint paint) {
            // TODO: 2018/9/5  
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            final Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ladder);
            canvas.drawBitmap(bitmap, null, new RectF(rect), paint);
            setOnDismissListener(new onDismissListener() {
                @Override
                public void onDismiss() {
                    bitmap.recycle();
                }
            });
        }
    }
}
