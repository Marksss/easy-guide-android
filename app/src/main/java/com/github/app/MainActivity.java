package com.github.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.EasyGuideBuilder;
import com.github.easyguide.LayerPopWindow;
import com.github.easyguide.RelativeGuideLayer;

public class MainActivity extends AppCompatActivity {
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.demo_container);
        findViewById(R.id.easy_guide_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EasyGuideBuilder(MainActivity.this).
                        addLayer(new CustomGuideLayer()).
                        build().
                        show();
            }
        });
    }
}
