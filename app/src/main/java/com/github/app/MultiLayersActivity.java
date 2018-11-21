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
                addLayer(getLayer0()).
                addLayer(getLayer1()).
                addLayer(getLayer2()).
                show();
        /*   EasyGuide ends  */
    }

    private CommonGuideLayer getLayer0() {
        CommonGuideLayer layer = new CommonGuideLayer(this).
                addHighlightTarget(findViewById(R.id.multi_guide_0)).
                withExtraView(LayoutInflater.from(this).inflate(R.layout.layer_multi_0, null), 0, (int) DisplayUtils.dp2px(MultiLayersActivity.this, 25f), Location.TO_BOTTOM, Location.ALIGN_RIGHT).
                addHighlightTarget(findViewById(R.id.multi_guide_1)).
                withExtraView(LayoutInflater.from(this).inflate(R.layout.layer_multi_1, null), 0, 0, Location.TO_TOP);
        layer.setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
            @Override
            public void onClick(int targetIndex, @NonNull ILayerController controller) {
                if (targetIndex == 0) {
                    controller.goNext();
                } else if (targetIndex == 1) {
                    Toast.makeText(MultiLayersActivity.this, "Second Layer", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MultiLayersActivity.this, "outside Layer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layer.setOnHighLightDrawListener(new CommonGuideLayer.OnHighLightDrawListener() {
            @Override
            public void onDraw(int index, @NonNull Rect rect, @NonNull Canvas canvas, @NonNull Paint paint) {
                canvas.drawRoundRect(new RectF(rect), 10, 10, paint);
            }
        });
        return layer;
    }

    private CommonGuideLayer getLayer1() {
        CommonGuideLayer layer = new CommonGuideLayer(this).
                addHighlightTarget(findViewById(R.id.multi_guide_circle)).
                withExtraView(LayoutInflater.from(this).inflate(R.layout.layer_multi_3, null),
                        (int) DisplayUtils.dp2px(MultiLayersActivity.this, -6f),
                        (int) DisplayUtils.dp2px(MultiLayersActivity.this, 50f),
                        Location.TO_BOTTOM);
        layer.setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
            @Override
            public void onClick(int targetIndex, @NonNull ILayerController controller) {
                if (targetIndex >= 0) {
                    controller.goNext();
                }
            }
        });
        layer.setOnHighLightDrawListener(new CommonGuideLayer.OnHighLightDrawListener() {
            @Override
            public void onDraw(int index, @NonNull Rect rect, @NonNull Canvas canvas, @NonNull Paint paint) {
                float cx = (rect.left + rect.right) / 2;
                float cy = (rect.top + rect.bottom) / 2;
                float radius = Math.max((rect.right - rect.left) / 2, (rect.bottom - rect.top) / 2) + 10;
                canvas.drawCircle(cx, cy, radius, paint);
            }
        });
        return layer;
    }

    private CommonGuideLayer getLayer2() {
        CommonGuideLayer layer = new CommonGuideLayer(this).
                addHighlightTarget(findViewById(R.id.multi_guide_ladder)).
                withExtraView(LayoutInflater.from(this).inflate(R.layout.layer_multi_2, null), 0, 0, Location.COVER).
                withExtraView(LayoutInflater.from(this).inflate(R.layout.layer_multi_4, null),
                        0,
                        (int) DisplayUtils.dp2px(MultiLayersActivity.this, 10f),
                        Location.TO_BOTTOM);
        layer.setOnHighLightDrawListener(null);
        return layer;
    }
}
