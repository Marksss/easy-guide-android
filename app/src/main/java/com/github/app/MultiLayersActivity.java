package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.client.ILayerController;
import com.github.easyguide.layer.CommonGuideLayer;
import com.github.easyguide.layer.Location;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shenxl on 2018/8/14.
 */
public class MultiLayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_layers);

        /*   EasyGuide starts  */
        new EasyGuideManager(MultiLayersActivity.this).
                addLayer(new MultiLayer0(MultiLayersActivity.this)).
                addLayer(new MultiLayer1(MultiLayersActivity.this)).
                addLayer(new MultiLayer2(MultiLayersActivity.this)).
                show();
        /*   EasyGuide ends  */
    }

    public class MultiLayer0 extends CommonGuideLayer {

        public MultiLayer0(Activity activity) {
            super(activity);
        }

        @Override
        protected void onViewCreated(@NonNull Context context){
            addHighlightTarget(findViewById(R.id.multi_guide_0));
            withExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_0, null), 0, 50, Location.TO_BOTTOM, Location.ALIGN_RIGHT);
            addHighlightTarget(findViewById(R.id.multi_guide_1));
            withExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_1, null), 0, 0, Location.TO_TOP);
            setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
                @Override
                public void onClick(int targetIndex, @NotNull ILayerController controller) {
                    if (targetIndex == 0) {
                        controller.goNext();
                    } else if (targetIndex == 1) {
                        Toast.makeText(MultiLayersActivity.this, "Second Layer", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MultiLayersActivity.this, "outside Layer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            setOnHighLightDrawListener(new OnHighLightDrawListener() {
                @Override
                public void onDraw(int index, @NotNull Rect rect, @NotNull Canvas canvas, @NotNull Paint paint) {
                    canvas.drawRoundRect(new RectF(rect), 10, 10, paint);
                }
            });
        }
    }

    public class MultiLayer1 extends CommonGuideLayer {
        public MultiLayer1(Activity activity) {
            super(activity);
        }

        @Override
        protected void onViewCreated(@NonNull Context context){
            addHighlightTarget(findViewById(R.id.multi_guide_circle));
            withExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_1, null), 0, 0, Location.TO_TOP);
            setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
                @Override
                public void onClick(int targetIndex, @NotNull ILayerController controller) {
                    if (targetIndex >= 0) {
                        controller.goNext();
                    }
                }
            });
            setOnHighLightDrawListener(new OnHighLightDrawListener() {
                @Override
                public void onDraw(int index, @NotNull Rect rect, @NotNull Canvas canvas, @NotNull Paint paint) {
                    float cx = (rect.left + rect.right)/2;
                    float cy = (rect.top + rect.bottom)/2;
                    float radius = Math.max((rect.right - rect.left)/2, (rect.bottom - rect.top)/2) + 10;
                    canvas.drawCircle(cx, cy, radius, paint);
                }
            });
        }
    }

    public class MultiLayer2 extends CommonGuideLayer {
        public MultiLayer2(Activity activity) {
            super(activity);
        }

        @Override
        protected void onViewCreated(@NotNull Context context) {
            addHighlightTarget(findViewById(R.id.multi_guide_ladder));
            withExtraView(LayoutInflater.from(context).inflate(R.layout.layer_multi_2, null), 0, 0, Location.COVER);
            setOnHighLightDrawListener(null);
        }
    }
}
