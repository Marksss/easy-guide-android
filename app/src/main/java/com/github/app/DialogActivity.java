package com.github.app;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/27.
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view1 = LayoutInflater.from(DialogActivity.this).inflate(R.layout.dialog,null);
                final AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)
                        .create();
                dialog.setTitle("This is dialog");
                dialog.setView(view1);
                final View view = dialog.getWindow().getDecorView();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        AbsGuideLayer basicLayer = new RelativeGuideLayer(DialogActivity.this).addTargetView(view1.findViewById(R.id.btn_left));
                        EasyGuideManager.create(basicLayer).with(DialogActivity.this).showLayer();
                    }
                });
                dialog.show();
            }
        });
    }
}
