package com.github.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.app.layer.MultiLayer0;
import com.github.easyguide.EasyGuideWindow;

public class MultiLayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_layers);

        findViewById(R.id.easy_guide_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EasyGuideWindow(MultiLayersActivity.this, new MultiLayer0()).show();
            }
        });
    }
}
