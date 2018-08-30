package com.github.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.app.layer.MultiLayer0;
import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.RelativeGuideLayer;
import com.github.easyguide.layer.RelativeLayerView;

/**
 * Created by shenxl on 2018/8/14.
 */
public class MultiLayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_layers);

        MultiLayer0 layer = new MultiLayer0(MultiLayersActivity.this);
        layer.setSingleClickListener(new RelativeGuideLayer.onSingleClickListener() {
            @Override
            public void onClick(int id, RelativeLayerView container, AbsGuideLayer.ILayerCallback callback) {
                callback.dismissCurrent();
            }
        });
        EasyGuideManager.create(layer).with(MultiLayersActivity.this).showLayer();
    }
}
