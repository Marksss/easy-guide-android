package com.github.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.easyguide.EasyGuideBuilder;

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
                        setGuideLayer(new CustomGuideLayer0()).
                        build().
                        show();
            }
        });
    }
}
