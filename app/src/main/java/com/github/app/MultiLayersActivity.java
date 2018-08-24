package com.github.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.app.layer.MultiLayer0;
import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.EasyGuideWindow;
import com.github.easyguide.RelativeGuideLayer;
import com.github.easyguide.RelativeLayerView;

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
        new EasyGuideWindow(MultiLayersActivity.this, layer).show(getFragmentManager(), "");
    }
}
