package com.github.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/23.
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.btn_basic_usage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsGuideLayer basicLayer = new RelativeGuideLayer(StartActivity.this).addTargetView(R.id.btn_basic_usage);
                EasyGuideManager.create(basicLayer).with(StartActivity.this).showLayer();
            }
        });

        findViewById(R.id.btn_multiple_layers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MultiLayersActivity.class));
            }
        });

        findViewById(R.id.btn_recyclerivew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RecyclerViewActivity.class));
            }
        });

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, DialogActivity.class));
            }
        });
    }
}