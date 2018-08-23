package com.github.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.app.layer.BasicLayer;
import com.github.app.layer.MultiLayer0;
import com.github.easyguide.EasyGuideWindow;

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
                new EasyGuideWindow(StartActivity.this, new BasicLayer()).show();
            }
        });

        findViewById(R.id.btn_multiple_layers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MultiLayersActivity.class));
            }
        });
    }
}